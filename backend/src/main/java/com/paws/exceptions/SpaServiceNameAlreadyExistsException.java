package com.paws.exceptions;

import org.springframework.http.HttpStatus;

public class SpaServiceNameAlreadyExistsException extends Exception implements PawsServiceException{
    @Override
    public HttpStatus getCode() {
        return HttpStatus.CONFLICT;
    }

    @Override
    public String getMessage() {
        return "Service name already exists.";
    }
}
