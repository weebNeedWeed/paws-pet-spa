package com.paws.models.customers;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.paws.entities.common.enums.AppointmentLocation;
import com.paws.payloads.request.MakeAppointmentItemRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MakeAppointmentRequest {
    @NotNull(message = "Địa điểm không được trống.")
    private AppointmentLocation location;

    @NotNull(message = "Thời gian không được trống.")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;
    private String note;

    @NotEmpty(message = "Phải đăng ký ít nhất 1 thú cưng.")
    @Valid
    private List<MakeAppointmentItemRequest> appointmentItems;

    public MakeAppointmentRequest() {
    }

    public AppointmentLocation getLocation() {
        return location;
    }

    public void setLocation(AppointmentLocation location) {
        this.location = location;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<MakeAppointmentItemRequest> getAppointmentItems() {
        return appointmentItems;
    }

    public void setAppointmentItems(List<MakeAppointmentItemRequest> appointmentItems) {
        this.appointmentItems = appointmentItems;
    }
}
