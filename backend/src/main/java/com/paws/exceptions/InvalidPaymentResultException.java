package com.paws.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidPaymentResultException extends Exception implements PawsServiceException{
    public InvalidPaymentResultException(String msg) {
        super(msg);
    }

    @Override
    public HttpStatus getCode() {
        return HttpStatus.BAD_REQUEST;
    }
}
