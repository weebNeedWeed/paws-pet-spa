package com.paws.controllers;

import com.paws.entities.common.enums.AppointmentItemStatus;
import com.paws.entities.common.enums.AppointmentStatus;
import com.paws.jobs.CountdownTimerJob;
import com.paws.models.websockets.MeasureWeightRequest;
import com.paws.models.websockets.StartAppointmentRequest;
import com.paws.models.websockets.WebSocketMessage;
import com.paws.services.employees.EmployeeService;
import com.paws.services.employees.payloads.AppointmentDto;
import com.paws.services.employees.payloads.AppointmentItemDto;
import com.paws.services.spasvcs.payloads.SpaSvcDto;
import org.quartz.*;
import org.springframework.cglib.core.Local;
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
    private final EmployeeService employeeService;
    private final Scheduler scheduler;

    public WebSocketController(EmployeeService employeeService, Scheduler scheduler) {
        this.employeeService = employeeService;
        this.scheduler = scheduler;
    }

    @MessageMapping("/start-appointment")
    @SendToUser("/queue/appointments")
    public WebSocketMessage<?> startAppointment(@Payload StartAppointmentRequest request, Principal user) throws Exception {
        AppointmentDto appointmentDto = employeeService.getAppointmentDetails(request.getAppointmentId());
        AppointmentItemDto appointmentItemDto = appointmentDto.getAppointmentItems().stream().filter(
                x -> x.getId() == request.getAppointmentItemId()
        ).findFirst().orElse(null);

        if(appointmentDto.getStatus() != AppointmentStatus.SCHEDULED &&
                appointmentDto.getStatus() != AppointmentStatus.IN_PROGRESS) {
            return new WebSocketMessage<Object>(null, "invalid_appointment");
        }

        if(appointmentItemDto.getStatus() == null
                || appointmentItemDto.getStatus() == AppointmentItemStatus.MEASURING_WEIGHT) {
            employeeService.initDetailedAppointment(request.getAppointmentItemId());
            return new WebSocketMessage<Object>(null, "start_measuring_weight");
        }

        if(appointmentItemDto.getStatus() == AppointmentItemStatus.IN_PROGRESS) {
            int index = appointmentItemDto.getDoneServiceIndex() == null ? -1 : appointmentItemDto.getDoneServiceIndex();
            if(index + 1 < appointmentItemDto.getSpaServices().size()) {
                if(!scheduler.checkExists(new JobKey(user.getName(), "timer"))) {
                    int currentDoingTaskIndex = index + 1;
                    startTimer(user.getName(),
                            request.getAppointmentItemId(),
                            currentDoingTaskIndex,
                            appointmentItemDto.getCurrentServiceEndingTime());
                }

                return new WebSocketMessage<String>(appointmentItemDto.getSpaServices().get(index + 1).getName(), "in_progress");
            }
        }

        return new WebSocketMessage<Object>(null, "done");
    }

    @MessageMapping("/measure-weight")
    @SendToUser("/queue/appointments")
    public WebSocketMessage<?> measureWeight(@Payload MeasureWeightRequest request, Principal user) throws Exception{
        employeeService.measurePetWeight(request.getAppointmentItemId(), request.getWeight());
        AppointmentDto appointmentDto = employeeService.getAppointmentDetails(request.getAppointmentId());
        AppointmentItemDto appointmentItemDto = appointmentDto.getAppointmentItems().stream().filter(
                x -> x.getId() == request.getAppointmentItemId()
        ).findFirst().orElse(null);

        int currentDoingTaskIndex = 0;
        SpaSvcDto service = appointmentItemDto.getSpaServices().get(currentDoingTaskIndex);
        long serviceId = service.getId();

        LocalDateTime endTime = employeeService.calculateEndTimeForService(
                appointmentItemDto.getPetWeight(), serviceId);

        startTimer(user.getName(), appointmentItemDto.getId(), currentDoingTaskIndex, endTime);

        return new WebSocketMessage<String>(service.getName(), "in_progress");
    }

    private void startTimer(String username, long appointmentItemId, int currentDoingTaskIndex, LocalDateTime endTime) throws SchedulerException {
        JobDetail job = JobBuilder.newJob(CountdownTimerJob.class)
                .withIdentity(username, "timer")
                .usingJobData(CountdownTimerJob.USERNAME, username)
                .usingJobData(CountdownTimerJob.APPOINTMENT_ITEM_ID, appointmentItemId)
                .usingJobData("currentDoingTaskIndex", currentDoingTaskIndex)
                .build();

        job.getJobDataMap().put("endTime", endTime);

        Date endAt = Date.from(endTime.plusSeconds(30).atZone(ZoneId.systemDefault()).toInstant());
        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule();
        Trigger trigger = TriggerBuilder.newTrigger()
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
