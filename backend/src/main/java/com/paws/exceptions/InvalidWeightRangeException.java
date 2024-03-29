package com.paws.exceptions;

import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

public class InvalidWeightRangeException extends Exception implements PawsServiceException{
    private BigDecimal min;
    private BigDecimal max;

    public InvalidWeightRangeException(BigDecimal min, BigDecimal max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public HttpStatus getCode() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getMessage() {
        return String.format("Khoảng trọng lượng %f đến %f là bất hợp lý", min, max);
    }
}
