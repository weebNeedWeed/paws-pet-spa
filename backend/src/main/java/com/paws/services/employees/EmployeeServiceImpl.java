package com.paws.services.employees;

import com.paws.entities.*;
import com.paws.repositories.*;
import com.paws.payloads.response.EmployeeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService{
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
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

    @Override
    public boolean isEmpDoingAppointment(String empUsername) {
        return employeeRepository.isEmpDoingAppointment(empUsername);
    }
}
