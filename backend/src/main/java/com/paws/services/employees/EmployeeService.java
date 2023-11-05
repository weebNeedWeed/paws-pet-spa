package com.paws.services.employees;

import com.paws.exceptions.*;
import com.paws.services.common.payloads.PagedResult;
import com.paws.services.employees.payloads.AppointmentDto;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {
    void approveAppointment(long appointmentId) throws AppointmentNotFoundException, InvalidAppointmentStatusException, NoEmployeeAssignedException, CannotScheduleAppointmentException;

    void assignEmployeeToAppointmentItem(long appointmentId,
                                          long appointmentItemId,
                                          long employeeId) throws AppointmentNotFoundException, AppointmentItemNotFoundException, EmployeeNotFoundException, InvalidAppointmentStatusException;

    void startAppointment(long appointmentId) throws AppointmentNotFoundException, InvalidAppointmentStatusException;

    PagedResult<AppointmentDto> getAllAppointments(Pageable pageable);
}

