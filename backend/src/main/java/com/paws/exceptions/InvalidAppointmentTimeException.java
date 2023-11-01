package com.paws.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidAppointmentTimeException extends Exception implements PawsServiceException{
    @Override
    public HttpStatus getCode() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getMessage() {
        return "Appointment time must be in one week after now.";
    }
}
