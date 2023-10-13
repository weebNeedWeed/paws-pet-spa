package com.paws.application.services.customers.common;

import com.paws.persistence.common.Gender;

public class CustomerAuthenticationResult {
    private CustomerDto customerDto;
    private String token;

    public CustomerAuthenticationResult() {
    }

    public CustomerDto getCustomerDto() {
        return customerDto;
    }

    public void setCustomerDto(CustomerDto customerDto) {
        this.customerDto = customerDto;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
