package com.paws.models.websockets;

public class MeasureWeightRequest {
    private long appointmentId;
    private long appointmentItemId;
    private float weight;

    public MeasureWeightRequest() {
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

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
}
