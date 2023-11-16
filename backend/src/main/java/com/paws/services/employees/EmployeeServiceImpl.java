package com.paws.services.employees;

import com.paws.entities.*;
import com.paws.entities.common.enums.AppointmentItemStatus;
import com.paws.entities.common.enums.AppointmentStatus;
import com.paws.exceptions.*;
import com.paws.jobs.CancelLateAppointmentJob;
import com.paws.repositories.*;
import com.paws.services.common.payloads.PagedResult;
import com.paws.services.employees.payloads.AppointmentDto;
import com.paws.services.employees.payloads.AppointmentItemDto;
import com.paws.services.spasvcs.payloads.SpaSvcDto;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class EmployeeServiceImpl implements EmployeeService{
    private final AppointmentRepository appointmentRepository;
    private final AppointmentItemRepository appointmentItemRepository;
    private final DetailedAppointmentItemRepository detailedAppointmentItemRepository;
    private final PetWeightRangeRepository petWeightRangeRepository;
    private final SpaServiceDetailRepository spaServiceDetailRepository;
    private final SpaServiceRepository spaServiceRepository;
    private final EmployeeRepository employeeRepository;
    private final Scheduler scheduler;

    @Autowired
    public EmployeeServiceImpl(AppointmentRepository appointmentRepository, AppointmentItemRepository appointmentItemRepository, DetailedAppointmentItemRepository detailedAppointmentItemRepository, PetWeightRangeRepository petWeightRangeRepository, SpaServiceDetailRepository spaServiceDetailRepository, SpaServiceRepository spaServiceRepository, EmployeeRepository employeeRepository, Scheduler scheduler) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentItemRepository = appointmentItemRepository;
        this.detailedAppointmentItemRepository = detailedAppointmentItemRepository;
        this.petWeightRangeRepository = petWeightRangeRepository;
        this.spaServiceDetailRepository = spaServiceDetailRepository;
        this.spaServiceRepository = spaServiceRepository;
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
    public void initDetailedAppointment(long appointmentItemId) throws AppointmentItemNotFoundException, InvalidAppointmentStatusException {
        AppointmentItem item = appointmentItemRepository.findById(appointmentItemId);
        if(item == null) {
            throw new AppointmentItemNotFoundException();
        }

        Appointment appointment = item.getAppointment();
        if(appointment.getStatus() != AppointmentStatus.SCHEDULED
                && appointment.getStatus() != AppointmentStatus.IN_PROGRESS) {
            throw new InvalidAppointmentStatusException(AppointmentStatus.SCHEDULED, appointment.getStatus());
        }

        if(appointment.getStatus() == AppointmentStatus.SCHEDULED) {
            appointment.setStatus(AppointmentStatus.IN_PROGRESS);
            appointmentRepository.save(appointment);
        }

        if(detailedAppointmentItemRepository.findById(item.getId()).isPresent()) {
            return;
        }

        DetailedAppointmentItem detailedItem = new DetailedAppointmentItem();
        detailedItem.setAppointmentItem(item);
        detailedItem.setStatus(AppointmentItemStatus.MEASURING_WEIGHT);

        detailedAppointmentItemRepository.save(detailedItem);
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
                    appointmentDto.setCustomerFullName(x.getCustomer().getFullName());

                    return appointmentDto;
                }).toList();

        result.setData(appointmentDtos);

        return result;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public AppointmentDto getAppointmentDetails(long appointmentId) throws AppointmentNotFoundException {
        Appointment appointment = appointmentRepository.findById(appointmentId);
        if(appointment == null) {
            throw new AppointmentNotFoundException();
        }

        AppointmentDto appointmentDto = new AppointmentDto();
        Customer customer = appointment.getCustomer();

        appointmentDto.setStatus(appointment.getStatus());
        appointmentDto.setNote(appointment.getNote());
        appointmentDto.setAppointmentTime(appointment.getAppointmentTime());
        appointmentDto.setId(appointment.getId());
        appointmentDto.setCreatedAt(appointment.getCreatedAt());
        appointmentDto.setUpdatedAt(appointment.getUpdatedAt());
        appointmentDto.setLocation(appointment.getLocation());
        appointmentDto.setCustomerFullName(customer.getFullName());

        appointmentDto.setAppointmentItems(new ArrayList<>());

        List<AppointmentItem> appointmentItems = appointment.getAppointmentItems();
        for(AppointmentItem item : appointmentItems) {
            AppointmentItemDto itemDto = new AppointmentItemDto();
            itemDto.setId(item.getId());
            itemDto.setPetType(item.getPetType().getName());
            itemDto.setPetName(item.getPetName());

            Set<SpaService> spaServices = item.getSpaServices();
            List<SpaSvcDto> spaSvcDtos = spaServices.stream().map(
                    x -> {
                        SpaSvcDto dto = new SpaSvcDto();
                        dto.setId(x.getId());
                        dto.setName(x.getName());
                        dto.setDescription(x.getDescription());

                        return dto;
                    }).sorted(Comparator.comparing(SpaSvcDto::getId)).toList();

            itemDto.setSpaServices(spaSvcDtos);

            DetailedAppointmentItem detailedItem = detailedAppointmentItemRepository.findById(item.getId()).orElse(null);
            if(detailedItem != null) {
                itemDto.setPetWeight(detailedItem.getPetWeight());
                itemDto.setStatus(detailedItem.getStatus());
                itemDto.setActualStartingTime(detailedItem.getActualStartingTime());
                itemDto.setCurrentServiceEndingTime(detailedItem.getCurrentServiceEndingTime());
                itemDto.setPetWeight(detailedItem.getPetWeight());
                itemDto.setDoneServiceIndex(detailedItem.getDoneServiceIndex());
                itemDto.setEndingTime(detailedItem.getEndingTime());
            }

            appointmentDto.getAppointmentItems().add(itemDto);
        }

        return appointmentDto;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void measurePetWeight(long appointmentItemId, double weight) {
        DetailedAppointmentItem detailedAppointmentItem = detailedAppointmentItemRepository.findById(appointmentItemId);
        AppointmentItem appointmentItem = appointmentItemRepository.findById(appointmentItemId);

        detailedAppointmentItem.setPetWeight(weight);
        detailedAppointmentItem.setActualStartingTime(LocalDateTime.now());
        detailedAppointmentItem.setStatus(AppointmentItemStatus.IN_PROGRESS);

        LocalDateTime now = LocalDateTime.now();
        long sum = 0;
        for(SpaService service: appointmentItem.getSpaServices()) {
            LocalDateTime endTime = calculateEndTimeForService(weight, service.getId());
            long seconds = Duration.between(now, endTime).toSeconds();
            sum += seconds;
        }

        int complementMins = 0;
        LocalDateTime appointmentEndTime = now.plusSeconds(sum).plusMinutes(complementMins);

        detailedAppointmentItem.setEndingTime(appointmentEndTime);

        detailedAppointmentItemRepository.save(detailedAppointmentItem);
    }

    @Override
    public LocalDateTime calculateEndTimeForService(double weight, long serviceId) {
        double floorWeight = Math.floor(weight * 10) / (double) 10;
        PetWeightRange range = petWeightRangeRepository
                .getPetWeightRangeByWeight(BigDecimal.valueOf(floorWeight));

        LocalDateTime now = LocalDateTime.now();

        // TODO: change it later
        float complementMins = 0;

        SpaService service = spaServiceRepository.getSpaServiceById(serviceId);
        float mins = complementMins + service.getDefaultEstimatedCompletionMinutes();

        SpaServiceDetail serviceDetail = spaServiceDetailRepository.findByServiceIdAndWeightRange(serviceId, range.getId().getMinWeight(), range.getId().getMaxWeight());
        if(serviceDetail != null)
            mins = complementMins + serviceDetail.getEstimatedCompletionMinutes();// estimated time plus 15 complement minutes

        return now.plusMinutes((int)mins);
    }
}
