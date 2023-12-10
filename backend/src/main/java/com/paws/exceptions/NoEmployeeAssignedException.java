package com.paws.exceptions;

import org.springframework.http.HttpStatus;

public class NoEmployeeAssignedException extends Exception implements PawsServiceException{
    @Override
    public HttpStatus getCode() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getMessage() {
        return "Không có nhân viên nào được sắp xếp";
    }
}
