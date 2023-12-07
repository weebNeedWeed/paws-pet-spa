package com.paws.services.customers;

import com.paws.exceptions.*;
import com.paws.payloads.request.MakeAppointmentItemRequest;
import com.paws.payloads.response.CustomerAuthenticationResult;
import com.paws.entities.common.enums.AppointmentLocation;
import com.paws.entities.common.enums.Gender;
import com.paws.payloads.response.CustomerDto;

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

    CustomerDto getProfile(String username) throws CustomerNotFoundException;
}
