package com.paws.models.websockets;

public class TimerResponse {
    private long secondsTillEndAppointment;
    private long secondsTillEndService;

    public TimerResponse() {
    }

    public long getSecondsTillEndAppointment() {
        return secondsTillEndAppointment;
    }

    public void setSecondsTillEndAppointment(long secondsTillEndAppointment) {
        this.secondsTillEndAppointment = secondsTillEndAppointment;
    }

    public long getSecondsTillEndService() {
        return secondsTillEndService;
    }

    public void setSecondsTillEndService(long secondsTillEndService) {
        this.secondsTillEndService = secondsTillEndService;
    }
}
