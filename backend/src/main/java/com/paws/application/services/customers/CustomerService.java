package com.paws.application.services.customers;

import com.paws.application.services.customers.common.CustomerAuthenticationResult;
import com.paws.persistence.common.Gender;

public interface CustomerService {
    CustomerAuthenticationResult login(String username,
                                       String password);

    CustomerAuthenticationResult register(String username,
                                          String password,
                                          String email,
                                          String fullName,
                                          String address,
                                          String phoneNumber,
                                          Gender gender);
}
