package com.paws.entities.common.enums;

public enum AppointmentItemStatus {
    MEASURING_WEIGHT("Đo cân nặng"),
    IN_PROGRESS("Đang thực hiện"),
    DONE("Hoàn thành");

    private final String value;

    AppointmentItemStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
