package com.paws.services.employees;

import com.paws.entities.Appointment;
import com.paws.entities.AppointmentItem;
import com.paws.entities.Employee;
import com.paws.entities.common.enums.AppointmentStatus;
import com.paws.exceptions.*;
import com.paws.jobs.CancelLateAppointmentJob;
import com.paws.repositories.AppointmentRepository;
import com.paws.repositories.EmployeeRepository;
import com.paws.services.common.payloads.PagedResult;
import com.paws.services.employees.payloads.AppointmentDto;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService{
    private final AppointmentRepository appointmentRepository;
    private final EmployeeRepository employeeRepository;
    private final Scheduler scheduler;

    @Autowired
    public EmployeeServiceImpl(AppointmentRepository appointmentRepository, EmployeeRepository employeeRepository, Scheduler scheduler) {
        this.appointmentRepository = appointmentRepository;
        this.employeeRepository = employeeRepository;
        this.scheduler = scheduler;
    }

    @Override
    @Transactional
    public void approveAppointment(long appointmentId) throws AppointmentNotFoundException, InvalidAppointmentStatusException, NoEmployeeAssignedException, CannotScheduleAppointmentException {
        Appointment appointment = appointmentRepository.findById(appointmentId);
        if(appointment == null) {
            throw new AppointmentNotFoundException();
        }

        if(appointment.getStatus() != AppointmentStatus.PENDING) {
            throw new InvalidAppointmentStatusException(AppointmentStatus.PENDING, appointment.getStatus());
        }

        for(AppointmentItem item: appointment.getAppointmentItems()) {
            if(item.getEmployee() == null) {
                throw new NoEmployeeAssignedException();
            }
        }

        JobDetail job = JobBuilder.newJob(CancelLateAppointmentJob.class)
                .usingJobData(CancelLateAppointmentJob.APPOINTMENT_ID, appointmentId).build();

        Date timeToStart = Date.from(appointment.getAppointmentTime()
                .plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant());

        Trigger trigger = TriggerBuilder.newTrigger()
                        .startAt(timeToStart)
                        .build();

        try {
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException ex) {
            throw new CannotScheduleAppointmentException();
        }

        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointmentRepository.save(appointment);
    }

    @Override
    @Transactional
    public void assignEmployeeToAppointmentItem(long appointmentId, long appointmentItemId, long employeeId) throws AppointmentNotFoundException, AppointmentItemNotFoundException, EmployeeNotFoundException, InvalidAppointmentStatusException {
        Appointment appointment = appointmentRepository.findById(appointmentId);
        if(appointment == null) {
            throw new AppointmentNotFoundException();
        }

        if(appointment.getStatus() != AppointmentStatus.PENDING) {
            throw new InvalidAppointmentStatusException(AppointmentStatus.PENDING, appointment.getStatus());
        }

        AppointmentItem item = appointment.getAppointmentItems().stream().filter(
                x -> x.getId().equals(appointmentItemId)
        ).findFirst().orElse(null);
        if(item == null) {
            throw new AppointmentItemNotFoundException();
        }

        Employee emp = employeeRepository.findById(employeeId);
        if(emp == null) {
            throw new EmployeeNotFoundException();
        }

        item.setEmployee(emp);

        appointmentRepository.save(appointment);
    }

    @Override
    @Transactional
    public void startAppointment(long appointmentId) throws AppointmentNotFoundException, InvalidAppointmentStatusException {
        Appointment appointment = appointmentRepository.findById(appointmentId);
        if(appointment == null) {
            throw new AppointmentNotFoundException();
        }

        if(appointment.getStatus() != AppointmentStatus.SCHEDULED) {
            throw new InvalidAppointmentStatusException(AppointmentStatus.SCHEDULED, appointment.getStatus());
        }

        appointment.setStatus(AppointmentStatus.IN_PROGRESS);

        appointmentRepository.save(appointment);
    }

    @Override
    public PagedResult<AppointmentDto> getAllAppointments(Pageable pageable) {
        Page<Appointment> appointments = appointmentRepository.findAll(pageable);

        PagedResult<AppointmentDto> result = new PagedResult<>();
        result.setPageIndex(appointments.getNumber());
        result.setPageSize(appointments.getSize());
        result.setTotalPages(appointments.getTotalPages());

        List<AppointmentDto> appointmentDtos = appointments.stream().map(
                x -> {
                    AppointmentDto appointmentDto = new AppointmentDto();
                    appointmentDto.setAppointmentTime(x.getAppointmentTime());
                    appointmentDto.setId(x.getId());
                    appointmentDto.setLocation(x.getLocation());
                    appointmentDto.setCreatedAt(x.getCreatedAt());
                    appointmentDto.setUpdatedAt(x.getUpdatedAt());
                    appointmentDto.setStatus(x.getStatus());

                    return appointmentDto;
                }).toList();

        result.setData(appointmentDtos);

        return result;
    }
}
