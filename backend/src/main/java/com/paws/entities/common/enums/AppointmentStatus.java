package com.paws.entities.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AppointmentStatus {
    PENDING("Đang chờ"),
    SCHEDULED("Đã đặt lịch"),
    IN_PROGRESS("Đang xử lý"),
    COMPLETED("Hoàn thành"),
    CANCELED("Bị huỷ bỏ");

    @JsonValue
    private final String value;

    AppointmentStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
