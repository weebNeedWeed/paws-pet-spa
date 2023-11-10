package com.paws.jobs;

import com.paws.entities.DetailedAppointmentItem;
import com.paws.models.websockets.TimerResponse;
import com.paws.repositories.AppointmentItemRepository;
import com.paws.repositories.DetailedAppointmentItemRepository;
import com.paws.repositories.SpaServiceRepository;
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
    public static final String SERVICE_ID = "serviceId";
    private final SimpMessagingTemplate messagingTemplate;
    private final AppointmentItemRepository appointmentItemRepository;
    private final DetailedAppointmentItemRepository detailedAppointmentItemRepository;
    private final SpaServiceRepository spaServiceRepository;

    @Autowired
    public CountdownTimerJob(SimpMessagingTemplate messagingTemplate, AppointmentItemRepository appointmentItemRepository, DetailedAppointmentItemRepository detailedAppointmentItemRepository, SpaServiceRepository spaServiceRepository) {
        this.messagingTemplate = messagingTemplate;
        this.appointmentItemRepository = appointmentItemRepository;
        this.detailedAppointmentItemRepository = detailedAppointmentItemRepository;
        this.spaServiceRepository = spaServiceRepository;
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
            detailed.setDoneServiceIndex(currentDoingTaskIndex);
            detailed.setCurrentServiceEndingTime(null);
            detailedAppointmentItemRepository.save(detailed);

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
