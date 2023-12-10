package com.paws.exceptions;

import org.springframework.http.HttpStatus;

public class UsernameAlreadyExistsException extends Exception implements PawsServiceException{
    @Override
    public HttpStatus getCode() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getMessage() {
        return "Tên tài khoản đã tồn tại";
    }
}
