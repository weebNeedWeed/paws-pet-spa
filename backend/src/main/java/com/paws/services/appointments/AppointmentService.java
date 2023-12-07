package com.paws.services.appointments;

import com.paws.entities.common.enums.AppointmentLocation;
import com.paws.exceptions.*;
import com.paws.payloads.common.PagedResult;
import com.paws.payloads.request.MakeAppointmentItemRequest;
import com.paws.payloads.response.AppointmentDto;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

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

    void makeAppointment(String username,
                         AppointmentLocation location,
                         LocalDateTime time,
                         String note,
                         List<MakeAppointmentItemRequest> appointmentItems) throws CustomerNotFoundException, InvalidAppointmentTimeException, PetTypeNotFoundException, SpaServiceNotFoundException;
}
