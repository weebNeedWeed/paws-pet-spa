package com.paws.application.services.customers;

import com.paws.application.common.errors.UsernameAlreadyExistsException;
import com.paws.application.services.customers.common.CustomerAuthenticationResult;
import com.paws.persistence.common.enums.AppointmentLocation;
import com.paws.persistence.common.enums.Gender;
import com.paws.persistence.entities.Appointment;

import java.time.LocalDateTime;

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

    Appointment makeAppointment(long customerId,
                                AppointmentLocation location,
                                LocalDateTime time,
                                String note,
                                int numPets);

    void cancelAppointment(long customerId, long appointmentId);

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
