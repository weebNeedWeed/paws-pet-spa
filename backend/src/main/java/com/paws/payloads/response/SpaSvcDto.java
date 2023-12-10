package com.paws.payloads.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SpaSvcDto {
    private long id;
    private String name;
    private String description;
    private BigDecimal defaultPrice;
    private float defaultEstimatedCompletionMinutes;
}
