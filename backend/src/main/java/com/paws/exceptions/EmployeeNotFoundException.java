package com.paws.exceptions;

import org.springframework.http.HttpStatus;

public class EmployeeNotFoundException extends Exception implements PawsServiceException{
    @Override
    public HttpStatus getCode() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getMessage() {
        return "Employee does not exist.";
    }
}
