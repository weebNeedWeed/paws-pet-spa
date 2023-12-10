package com.paws.payloads.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDetailDto {
    private BigDecimal minWeight;
    private BigDecimal maxWeight;
    private BigDecimal price;
    private float estimatedCompletionMinutes;
}
