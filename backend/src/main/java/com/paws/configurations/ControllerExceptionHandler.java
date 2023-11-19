package com.paws.configurations;

import com.paws.exceptions.PawsServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice(basePackages = {"com.paws.controllers"})
public class ControllerExceptionHandler {
    @ExceptionHandler({AccessDeniedException.class})
    public ModelAndView handleAccessDenied(Exception ex, WebRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error/error-403");

        return modelAndView;
    }

    @ExceptionHandler({Exception.class})
    public ModelAndView handlePawsException(Exception ex, WebRequest request) {
        ModelAndView modelAndView = new ModelAndView();

        if(!(ex instanceof PawsServiceException)) {
            modelAndView.setViewName("error/common");
            return modelAndView;
        }

        modelAndView.setViewName("error/error-500");
        modelAndView.addObject("errorMsg", ex.getMessage());

        PawsServiceException pawsError = (PawsServiceException) ex;
        if(pawsError.getCode() == HttpStatus.NOT_FOUND) {
            modelAndView.setViewName("error/error-404");
        }

        return modelAndView;
    }
}
