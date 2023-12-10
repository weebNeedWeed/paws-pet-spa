package com.paws.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidAppointmentTimeException extends Exception implements PawsServiceException{
    @Override
    public HttpStatus getCode() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getMessage() {
        return "Thời gian hẹn phải trong vòng 2 tuần kể từ bây giờ";
    }
}
