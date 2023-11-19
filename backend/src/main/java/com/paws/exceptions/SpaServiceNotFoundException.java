package com.paws.exceptions;

import org.springframework.http.HttpStatus;

public class SpaServiceNotFoundException extends Exception implements PawsServiceException{
    @Override
    public HttpStatus getCode() {
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public String getMessage() {
        return "Service not found.";
    }
}
