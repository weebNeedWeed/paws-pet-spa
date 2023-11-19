package com.paws.services.employees;

import com.paws.entities.*;
import com.paws.entities.common.enums.AppointmentItemStatus;
import com.paws.entities.common.enums.AppointmentStatus;
import com.paws.exceptions.*;
import com.paws.jobs.CancelLateAppointmentJob;
import com.paws.repositories.*;
import com.paws.payloads.common.PagedResult;
import com.paws.payloads.response.AppointmentDto;
import com.paws.payloads.response.AppointmentItemDto;
import com.paws.payloads.response.EmployeeDto;
import com.paws.payloads.response.SpaSvcDto;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.stream.Collectors;

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
    public List<EmployeeDto> getAll(Set<String> roles) {
        List<Employee> employees = employeeRepository.getAllEmployees(roles);

        List<EmployeeDto> dtos = employees.stream().map(x -> {
            EmployeeDto dto = new EmployeeDto();
            dto.setId(x.getId());
            dto.setEmail(x.getEmail());
            dto.setAddress(x.getAddress());
            dto.setGender(x.getGender());
            dto.setFullName(x.getFullName());
            dto.setCreatedAt(x.getCreatedAt());
            dto.setDateOfBirth(x.getDateOfBirth());
            dto.setPhoneNumber(x.getPhoneNumber());
            dto.setUsername(x.getUsername());
            dto.setIdentityNumber(x.getIdentityNumber());
            dto.setFirstWorkingDay(x.getFirstWorkingDay());
            dto.setQuitDate(x.getQuitDate());
            dto.setRoles(x.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));

            return dto;
        }).toList();

        return dtos;
    }
}
