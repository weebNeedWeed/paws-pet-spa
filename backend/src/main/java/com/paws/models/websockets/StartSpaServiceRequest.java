package com.paws.models.websockets;

public class StartSpaServiceRequest {
    private long appointmentId;
    private long appointmentItemId;
    private int taskIndex;

    public StartSpaServiceRequest() {
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

    public int getTaskIndex() {
        return taskIndex;
    }

    public void setTaskIndex(int taskIndex) {
        this.taskIndex = taskIndex;
    }
}
