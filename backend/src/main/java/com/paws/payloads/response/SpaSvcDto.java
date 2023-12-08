package com.paws.payloads.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpaSvcDto {
    private long id;
    private String name;
    private String description;
    private BigDecimal defaultPrice;
    private float defaultEstimatedCompletionMinutes;
}