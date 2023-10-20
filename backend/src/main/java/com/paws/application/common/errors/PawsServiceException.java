package com.paws.application.common.errors;

import org.springframework.http.HttpStatus;

public interface PawsServiceException {
    HttpStatus getCode();

    String getMessage();
}
