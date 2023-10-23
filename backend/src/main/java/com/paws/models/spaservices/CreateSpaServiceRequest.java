package com.paws.models.spaservices;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateSpaServiceRequest {
    @NotEmpty(message = "Tên không được rỗng.")
    private String name;
    private String description;

    @DecimalMin(message = "Giá tiền phải lớn hơn 1000 VND.", value = "1000")
    @NotNull(message = "Giá tiền không được rỗng")
    private BigDecimal defaultPrice;
    
    @DecimalMin(message = "Thời gian phải lớn hơn 1 phút.", value = "1.0")
    private float defaultEstimatedCompletionMinutes;
}
