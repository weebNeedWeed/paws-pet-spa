package com.paws.application.services.spasvcs.common;

public class SpaSvcDto {
    private long id;
    private String name;
    private String description;
    private float defaultPrice;
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

    public float getDefaultPrice() {
        return defaultPrice;
    }

    public void setDefaultPrice(float defaultPrice) {
        this.defaultPrice = defaultPrice;
    }

    public float getDefaultEstimatedCompletionMinutes() {
        return defaultEstimatedCompletionMinutes;
    }

    public void setDefaultEstimatedCompletionMinutes(float defaultEstimatedCompletionMinutes) {
        this.defaultEstimatedCompletionMinutes = defaultEstimatedCompletionMinutes;
    }
}
