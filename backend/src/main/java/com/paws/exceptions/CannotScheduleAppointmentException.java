package com.paws.exceptions;

import org.springframework.http.HttpStatus;

public class CannotScheduleAppointmentException extends Exception implements PawsServiceException{
    @Override
    public HttpStatus getCode() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    @Override
    public String getMessage() {
        return "Can not schedule the appointment.";
    }
}
