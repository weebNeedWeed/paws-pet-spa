package com.paws.controllers;

import com.paws.entities.common.enums.AppointmentItemStatus;
import com.paws.entities.common.enums.AppointmentStatus;
import com.paws.exceptions.AppointmentItemNotFoundException;
import com.paws.exceptions.BillNotFoundException;
import com.paws.jobs.CountdownTimerJob;
import com.paws.models.websockets.*;
import com.paws.payloads.response.BillDto;
import com.paws.services.appointments.AppointmentService;
import com.paws.payloads.response.AppointmentDto;
import com.paws.payloads.response.AppointmentItemDto;
import com.paws.payloads.response.SpaSvcDto;
import com.paws.services.payments.PaymentService;
import com.paws.services.urlShortens.UrlShortenService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Controller
public class WebSocketController {
    private final Scheduler scheduler;
    private final AppointmentService appointmentService;
    private final PaymentService paymentService;
    private final UrlShortenService urlShortenService;

    @Autowired
    public WebSocketController(Scheduler scheduler, AppointmentService appointmentService, PaymentService paymentService, UrlShortenService urlShortenService) {
        this.scheduler = scheduler;
        this.appointmentService = appointmentService;
        this.paymentService = paymentService;
        this.urlShortenService = urlShortenService;
    }

    @MessageMapping("/start-appointment")
    @SendToUser("/queue/appointments")
    public WebSocketMessage<?> startAppointment(@Payload StartAppointmentRequest request, Principal user) throws Exception {
        AppointmentDto appointmentDto = appointmentService.getDetails(request.getAppointmentId());
        AppointmentItemDto appointmentItemDto = appointmentDto.getAppointmentItems().stream().filter(
                x -> x.getId() == request.getAppointmentItemId()
        ).findFirst().orElseThrow();

        if(appointmentDto.getStatus() != AppointmentStatus.SCHEDULED &&
                appointmentDto.getStatus() != AppointmentStatus.IN_PROGRESS) {
            return new WebSocketMessage<>(null, "invalid_appointment");
        }

        if(appointmentItemDto.getStatus() == null
                || appointmentItemDto.getStatus() == AppointmentItemStatus.MEASURING_WEIGHT) {
            appointmentService.initDetailed(request.getAppointmentItemId());
            return new WebSocketMessage<>(null, "start_measuring_weight");
        }

        if(appointmentItemDto.getStatus() == AppointmentItemStatus.IN_PROGRESS) {
            int index = appointmentItemDto.getDoneServiceIndex() == null ? -1 : appointmentItemDto.getDoneServiceIndex();
            if(index + 1 < appointmentItemDto.getSpaServices().size()) {
                int currentDoingTaskIndex = index + 1;
                if(!scheduler.checkExists(new JobKey(user.getName() + appointmentItemDto.getId() + currentDoingTaskIndex, "timer"))) {
                    try {
                        startTimer(user.getName(),
                                request.getAppointmentItemId(),
                                currentDoingTaskIndex,
                                appointmentItemDto.getCurrentServiceEndingTime());
                    } catch (RuntimeException ex) {
                        return new WebSocketMessage<>(null, "done");
                    }
                }

                return new WebSocketMessage<>(appointmentItemDto.getSpaServices().get(index + 1), "in_progress");
            }

            for(AppointmentItemDto item : appointmentDto.getAppointmentItems()) {
                if(item.getId() != request.getAppointmentItemId() && item.getStatus() != AppointmentItemStatus.DONE) {
                    return new WebSocketMessage<>(null, "done");
                }
            }
            return startPayment(appointmentDto.getId());
        }

        return new WebSocketMessage<>(null, "done");
    }

    @MessageMapping("/measure-weight")
    @SendToUser("/queue/appointments")
    public WebSocketMessage<?> measureWeight(@Payload MeasureWeightRequest request, Principal user) throws Exception{
        appointmentService.measurePetWeight(request.getAppointmentItemId(), request.getWeight());

        StartSpaServiceRequest req = new StartSpaServiceRequest();
        req.setTaskIndex(0);
        req.setAppointmentId(request.getAppointmentId());
        req.setAppointmentItemId(request.getAppointmentItemId());

        return startSpaService(req, user);
    }

    @MessageMapping("/paid")
    @SendToUser("/queue/appointments")
    public WebSocketMessage<?> setPaidBill(long appointmentId) throws AppointmentItemNotFoundException, BillNotFoundException {
        appointmentService.setPaidBill(appointmentId);
        return new WebSocketMessage<>(null, "payment_success");
    }

    public WebSocketMessage<?> startSpaService(StartSpaServiceRequest request, Principal user) throws Exception{
        AppointmentDto appointmentDto = appointmentService.getDetails(request.getAppointmentId());
        AppointmentItemDto appointmentItemDto = appointmentDto.getAppointmentItems().stream().filter(
                x -> x.getId() == request.getAppointmentItemId()
        ).findFirst().orElseThrow();

        if(request.getTaskIndex() >= appointmentItemDto.getSpaServices().size()) {
            for(AppointmentItemDto item : appointmentDto.getAppointmentItems()) {
                if(item.getId() != request.getAppointmentItemId() && item.getStatus() != AppointmentItemStatus.DONE) {
                    appointmentService.setDoneItem(appointmentItemDto.getId());
                    return new WebSocketMessage<>(null, "done");
                }
            }

            appointmentService.generateBill(appointmentDto.getId(), user.getName());
            return startPayment(appointmentDto.getId());
        }

        SpaSvcDto service = appointmentItemDto.getSpaServices().get(request.getTaskIndex());
        long serviceId = service.getId();

        LocalDateTime endTime = appointmentService.calculateEndTimeForService(
                appointmentItemDto.getPetWeight(), serviceId);

        startTimer(user.getName(), appointmentItemDto.getId(), request.getTaskIndex(), endTime);

        return new WebSocketMessage<>(service, "in_progress");
    }

    private WebSocketMessage<?> startPayment(long appointmentId) throws BillNotFoundException {
        BillDto bill = appointmentService.getBill(appointmentId);
        String paymentUrl = paymentService.createPaymentUrl(bill);

        String shortened = urlShortenService.shorten("payment" + bill.getId(), paymentUrl);

        PaymentMessage msg = new PaymentMessage();
        msg.setBill(bill);
        msg.setPaymentUrl(shortened);

        return new WebSocketMessage<>(msg, "payment");
    }

    private void startTimer(String username, long appointmentItemId, int currentDoingTaskIndex, LocalDateTime endTime) throws SchedulerException {
        JobDetail job = JobBuilder.newJob(CountdownTimerJob.class)
                .withIdentity(username + appointmentItemId + currentDoingTaskIndex, "timer")
                .usingJobData(CountdownTimerJob.USERNAME, username)
                .usingJobData(CountdownTimerJob.APPOINTMENT_ITEM_ID, appointmentItemId)
                .usingJobData("currentDoingTaskIndex", currentDoingTaskIndex)
                .build();

        job.getJobDataMap().put("endTime", endTime);

        Date endAt = Date.from(endTime.plusSeconds(30).atZone(ZoneId.systemDefault()).toInstant());

        if(endAt.before(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))) {
            throw new RuntimeException("Appointment is out of time");
        }

        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule();
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(username + appointmentItemId + currentDoingTaskIndex,"trigger")
                .startNow()
                .forJob(job)
                .withSchedule(simpleScheduleBuilder
                    .withIntervalInSeconds(15)
                    .repeatForever())
                .endAt(endAt)
                .build();

        scheduler.scheduleJob(job, trigger);
    }
}
