package com.paws.configurations;

import com.paws.exceptions.PawsServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice(basePackages = "com.paws.restcontrollers")
public class RestControllerExceptionHandler {
    @ExceptionHandler({BadCredentialsException.class})
    public ProblemDetail handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(HttpStatus.UNAUTHORIZED, "Invalid credentials.");

        return problemDetail;
    }

    @ExceptionHandler({Exception.class})
    public ProblemDetail handlePawsException(Exception ex, WebRequest request) {
        if(!(ex instanceof PawsServiceException)) {
            return ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        PawsServiceException pawsError = (PawsServiceException) ex;

        return ProblemDetail.forStatusAndDetail(pawsError.getCode(), pawsError.getMessage());
    }
}
