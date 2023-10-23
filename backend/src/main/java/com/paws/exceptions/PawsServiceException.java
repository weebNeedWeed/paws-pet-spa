package com.paws.exceptions;

import org.springframework.http.HttpStatus;

public interface PawsServiceException {
    HttpStatus getCode();

    String getMessage();
}
