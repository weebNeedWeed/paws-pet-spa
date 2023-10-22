package com.paws.presentation.controllers.contracts.spaservices;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class CreateSpaServiceRequest {
    @NotEmpty(message = "Tên không được rỗng.")
    private String name;
    private String description;

    @DecimalMin(message = "Giá tiền phải lớn hơn 1000 VND.", value = "1000")
    @NotNull(message = "Giá tiền không được rỗng")
    private BigDecimal defaultPrice;
    
    @DecimalMin(message = "Thời gian phải lớn hơn 1 phút.", value = "1.0")
    private float defaultEstimatedCompletionMinutes;

    public CreateSpaServiceRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getDefaultPrice() {
        return defaultPrice;
    }

    public void setDefaultPrice(BigDecimal defaultPrice) {
        this.defaultPrice = defaultPrice;
    }

    public float getDefaultEstimatedCompletionMinutes() {
        return defaultEstimatedCompletionMinutes;
    }

    public void setDefaultEstimatedCompletionMinutes(float defaultEstimatedCompletionMinutes) {
        this.defaultEstimatedCompletionMinutes = defaultEstimatedCompletionMinutes;
    }
}
