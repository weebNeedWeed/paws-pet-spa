package com.paws.exceptions;

import org.springframework.http.HttpStatus;

public class CustomerNotFoundException extends Exception implements PawsServiceException{
    @Override
    public HttpStatus getCode() {
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public String getMessage() {
        return "Không tìm thấy khách hàng";
    }
}
