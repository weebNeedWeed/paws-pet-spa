package com.paws.services.employees;

import com.paws.exceptions.*;
import com.paws.services.common.payloads.PagedResult;
import com.paws.services.employees.payloads.AppointmentDto;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface EmployeeService {
    void approveAppointment(long appointmentId) throws AppointmentNotFoundException, InvalidAppointmentStatusException, NoEmployeeAssignedException, CannotScheduleAppointmentException;

    void assignEmployeeToAppointmentItem(long appointmentId,
                                          long appointmentItemId,
                                          long employeeId) throws AppointmentNotFoundException, AppointmentItemNotFoundException, EmployeeNotFoundException, InvalidAppointmentStatusException;

    void initDetailedAppointment(long appointmentItemId) throws AppointmentItemNotFoundException, InvalidAppointmentStatusException;

    PagedResult<AppointmentDto> getAllAppointments(Pageable pageable);

    AppointmentDto getAppointmentDetails(long appointmentId) throws AppointmentNotFoundException;

    void measurePetWeight(long appointmentItemId, double weight);

    LocalDateTime calculateEndTimeForService(double weight, long serviceId);
}

