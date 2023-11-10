package com.paws.models.websockets;

public class StartAppointmentRequest {
    private long appointmentId;
    private long appointmentItemId;

    public StartAppointmentRequest() {
    }

    public long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public long getAppointmentItemId() {
        return appointmentItemId;
    }

    public void setAppointmentItemId(long appointmentItemId) {
        this.appointmentItemId = appointmentItemId;
    }
}
