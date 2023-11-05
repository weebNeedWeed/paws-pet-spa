package com.paws.dataseeders;

import com.paws.jobs.CancelLateAppointmentJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class TestCmdLineRunner2 implements CommandLineRunner{
    @Autowired
    private Scheduler scheduler;

    @Override
    public void run(String... args) throws Exception {
//        JobDetail job = JobBuilder.newJob(SimpleJob.class)
//                .build();
//
//        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule();
//
//        Trigger trigger = TriggerBuilder.newTrigger()
//                .withSchedule(simpleScheduleBuilder.withIntervalInSeconds(1).repeatForever())
//                .startNow().build();
//
//        scheduler.scheduleJob(job, trigger);
    }
}
