package com.paws.restcontrollers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseRestController {
    protected ResponseEntity<?> validationProblemDetails(BindingResult bindingResult) {
        List<ObjectError> errors = bindingResult.getAllErrors();
        Map<String, List<String>> errorMap = new HashMap<>();

        errors.forEach(x -> {
            String field = ((FieldError)x).getField();
            if(!errorMap.containsKey(field)) {
                errorMap.put(field, new ArrayList<>());
            }

            errorMap.get(field).add(x.getDefaultMessage());
        });

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setDetail("Một hoặc vài trường xảy ra lỗi");
        problemDetail.setProperty("errors", errorMap);

        return ResponseEntity.badRequest().body(problemDetail);
    }
}
