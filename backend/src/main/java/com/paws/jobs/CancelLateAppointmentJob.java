package com.paws.jobs;

import com.paws.entities.Appointment;
import com.paws.entities.common.enums.AppointmentStatus;
import com.paws.exceptions.AppointmentNotFoundException;
import com.paws.repositories.AppointmentRepository;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CancelLateAppointmentJob implements Job {
    public static final String APPOINTMENT_ID = "appointmentId";

    private final AppointmentRepository appointmentRepository;

    @Autowired
    public CancelLateAppointmentJob(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException{
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        long appointmentId = dataMap.getLong(APPOINTMENT_ID);

        Appointment appointment = appointmentRepository.findById(appointmentId);

        if(appointment == null) {
            JobExecutionException ex = new JobExecutionException();
            ex.unscheduleFiringTrigger();

            throw ex;
        }

        if(appointment.getStatus() == AppointmentStatus.SCHEDULED) {
            appointment.setStatus(AppointmentStatus.CANCELED);
            appointmentRepository.save(appointment);
        }
    }
}
