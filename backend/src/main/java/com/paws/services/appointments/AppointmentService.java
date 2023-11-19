package com.paws.services.appointments;

import com.paws.exceptions.*;
import com.paws.payloads.common.PagedResult;
import com.paws.payloads.response.AppointmentDto;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface AppointmentService {
    void approve(long appointmentId) throws AppointmentNotFoundException, InvalidAppointmentStatusException, NoEmployeeAssignedException, CannotScheduleAppointmentException;

    void assignEmployee(long appointmentId,
                         long appointmentItemId,
                         long employeeId) throws AppointmentNotFoundException, AppointmentItemNotFoundException, EmployeeNotFoundException, InvalidAppointmentStatusException;

    void initDetailed(long appointmentItemId) throws AppointmentItemNotFoundException, InvalidAppointmentStatusException;

    PagedResult<AppointmentDto> getAll(Pageable pageable);

    AppointmentDto getDetails(long appointmentId) throws AppointmentNotFoundException;

    void measurePetWeight(long appointmentItemId, double weight);

    LocalDateTime calculateEndTimeForService(double weight, long serviceId);
}
