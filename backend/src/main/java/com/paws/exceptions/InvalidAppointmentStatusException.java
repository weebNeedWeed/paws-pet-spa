package com.paws.exceptions;

import com.paws.entities.common.enums.AppointmentStatus;
import org.springframework.http.HttpStatus;

public class InvalidAppointmentStatusException extends Exception implements PawsServiceException{
    private final AppointmentStatus expected;
    private final AppointmentStatus got;

    public InvalidAppointmentStatusException(AppointmentStatus expected, AppointmentStatus got) {
        this.expected = expected;
        this.got = got;
    }

    @Override
    public HttpStatus getCode() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getMessage() {
        return String.format("Invalid appointment status: expected %s, got %s", expected.getValue(), got.getValue());
    }
}
