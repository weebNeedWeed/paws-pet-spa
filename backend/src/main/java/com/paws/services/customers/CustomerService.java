package com.paws.services.customers;

import com.paws.exceptions.UsernameAlreadyExistsException;
import com.paws.services.customers.common.CustomerAuthenticationResult;
import com.paws.entities.common.enums.AppointmentLocation;
import com.paws.entities.common.enums.Gender;
import com.paws.entities.Appointment;

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
