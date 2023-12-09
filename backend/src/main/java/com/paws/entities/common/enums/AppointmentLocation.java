package com.paws.entities.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AppointmentLocation {
    AT_HOME("Tại nhà"),
    AT_SHOP("Tại chi nhánh");

    @JsonValue
    private final String value;

    AppointmentLocation(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
