package com.paws.application.services.spasvcs.common;

import java.math.BigDecimal;

public class SpaSvcDto {
    private long id;
    private String name;
    private String description;
    private BigDecimal defaultPrice;
    private float defaultEstimatedCompletionMinutes;

    public SpaSvcDto() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
