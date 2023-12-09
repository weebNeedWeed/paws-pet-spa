package com.paws.jobs;

import com.paws.controllers.WebSocketController;
import com.paws.entities.AppointmentItem;
import com.paws.entities.DetailedAppointmentItem;
import com.paws.models.websockets.StartSpaServiceRequest;
import com.paws.models.websockets.TimerResponse;
import com.paws.models.websockets.WebSocketMessage;
import com.paws.repositories.AppointmentItemRepository;
import com.paws.repositories.DetailedAppointmentItemRepository;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class CountdownTimerJob implements Job {
    public static final String USERNAME = "username";
    public static final String APPOINTMENT_ITEM_ID = "appointmentItemId";
    private final SimpMessagingTemplate messagingTemplate;
    private final AppointmentItemRepository appointmentItemRepository;
    private final DetailedAppointmentItemRepository detailedAppointmentItemRepository;
    private final WebSocketController webSocketController;

    @Autowired
    public CountdownTimerJob(SimpMessagingTemplate messagingTemplate, AppointmentItemRepository appointmentItemRepository, DetailedAppointmentItemRepository detailedAppointmentItemRepository, WebSocketController webSocketController) {
        this.messagingTemplate = messagingTemplate;
        this.appointmentItemRepository = appointmentItemRepository;
        this.detailedAppointmentItemRepository = detailedAppointmentItemRepository;
        this.webSocketController = webSocketController;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        String username = dataMap.getString(USERNAME);
        long appointmentItemId = dataMap.getLong(APPOINTMENT_ITEM_ID);
        int currentDoingTaskIndex = dataMap.getInt("currentDoingTaskIndex");
        LocalDateTime endTime = (LocalDateTime) dataMap.get("endTime");
        LocalDateTime now = LocalDateTime.now();

        DetailedAppointmentItem detailed = detailedAppointmentItemRepository.findById(appointmentItemId);
        if(detailed.getCurrentServiceEndingTime() == null) {
            detailed.setCurrentServiceEndingTime(endTime);
            detailedAppointmentItemRepository.save(detailed);
        } else {
            endTime = detailed.getCurrentServiceEndingTime();
        }

        if(now.isAfter(endTime)) {
            if(detailed.getDoneServiceIndex() == null || detailed.getDoneServiceIndex() != currentDoingTaskIndex) {
                detailed.setDoneServiceIndex(currentDoingTaskIndex);
                detailed.setCurrentServiceEndingTime(null);
                detailedAppointmentItemRepository.save(detailed);

                WebSocketMessage<Object> msg = new WebSocketMessage<>();
                msg.setMessage("task_completed");

                messagingTemplate.convertAndSendToUser(username, "/queue/appointments", msg);

                try {
                    AppointmentItem item = appointmentItemRepository.findById(appointmentItemId);
                    StartSpaServiceRequest req = new StartSpaServiceRequest();
                    req.setAppointmentId(item.getAppointment().getId());
                    req.setTaskIndex(currentDoingTaskIndex + 1);
                    req.setAppointmentItemId(appointmentItemId);

                    messagingTemplate.convertAndSendToUser(username, "/queue/appointments", webSocketController.startSpaService(req, () -> username));
                } catch (Exception ex) {
                    throw new JobExecutionException(ex);
                }
            }

            return;
        }

        Duration serviceRemaining = Duration.between(now, endTime);
        long serviceSecsRemaining = serviceRemaining.toSeconds();

        Duration appointmentRemaining = Duration.between(now, detailed.getEndingTime());
        long appointmentSecsRemaining = appointmentRemaining.toSeconds();

        TimerResponse response = new TimerResponse();
        response.setSecondsTillEndService(serviceSecsRemaining);
        response.setSecondsTillEndAppointment(appointmentSecsRemaining);

        messagingTemplate.convertAndSendToUser(username, "/queue/timers", response);
    }
}
