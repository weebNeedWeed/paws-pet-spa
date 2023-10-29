package com.paws.services.weightranges.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WeightRangeDto {
    private BigDecimal minWeight;
    private BigDecimal maxWeight;
}
