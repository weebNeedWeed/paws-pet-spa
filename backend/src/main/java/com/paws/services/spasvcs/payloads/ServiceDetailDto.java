package com.paws.services.spasvcs.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDetailDto {
    private BigDecimal minWeight;
    private BigDecimal maxWeight;
    private BigDecimal price;
    private float estimatedCompletionMinutes;
}
