package com.paws.models.spaservices;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDetailByWeightRange {
    private BigDecimal minWeight;
    private BigDecimal maxWeight;

    @DecimalMin(message = "Giá tiền phải lớn hơn 1000 VND.", value = "1000")
    @NotNull(message = "Giá tiền không được rỗng")
    private BigDecimal price;

    @DecimalMin(message = "Thời gian phải lớn hơn 1 phút.", value = "1.0")
    private float estimatedCompletionMinutes;
}
