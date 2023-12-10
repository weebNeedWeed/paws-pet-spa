package com.paws.services.employees;

import com.paws.exceptions.*;
import com.paws.payloads.common.PagedResult;
import com.paws.payloads.response.AppointmentDto;
import com.paws.payloads.response.EmployeeDto;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EmployeeService {
    List<EmployeeDto> getAll(Set<String> roles);

    boolean isEmpDoingAppointment(String empUsername);
}

