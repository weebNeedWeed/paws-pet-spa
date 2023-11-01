package com.paws.services.customers;

import com.paws.exceptions.*;
import com.paws.services.customers.payloads.MakeAppointmentItemRequest;
import com.paws.services.customers.payloads.CustomerAuthenticationResult;
import com.paws.entities.common.enums.AppointmentLocation;
import com.paws.entities.common.enums.Gender;
import com.paws.entities.Appointment;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomerService {
    CustomerAuthenticationResult login(String username,
                                       String password);

    CustomerAuthenticationResult register(String username,
                                          String password,
                                          String email,
                                          String fullName,
                                          String address,
                                          String phoneNumber,
                                          Gender gender) throws UsernameAlreadyExistsException;

    void makeAppointment(long customerId,
                                AppointmentLocation location,
                                LocalDateTime time,
                                String note,
                                List<MakeAppointmentItemRequest> appointmentItems) throws CustomerNotFoundException, InvalidAppointmentTimeException, PetTypeNotFoundException, SpaServiceNotFoundException;

    void cancelAppointment(long customerId, long appointmentId) throws CustomerNotFoundException, AppointmentNotFoundException;

    void updateProfile(long customerId,
                       String email,
                       String fullName,
                       String address,
                       String phoneNumber,
                       Gender gender);

    void changePassword(long customerId,
                        String oldPassword,
                        String newPassword);
}
