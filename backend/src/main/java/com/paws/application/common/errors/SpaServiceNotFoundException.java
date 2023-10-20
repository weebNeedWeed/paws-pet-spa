package com.paws.application.common.errors;

import org.springframework.http.HttpStatus;

public class SpaServiceNotFoundException extends Exception implements PawsServiceException{
    @Override
    public HttpStatus getCode() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getMessage() {
        return "Service not found.";
    }
}
