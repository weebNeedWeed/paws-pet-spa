package com.paws.models.websockets;

public class WebSocketMessage <TPayload>{
    private TPayload payload;
    private String message;

    public WebSocketMessage() {
    }

    public WebSocketMessage(TPayload payload, String message) {
        this.payload = payload;
        this.message = message;
    }

    public TPayload getPayload() {
        return payload;
    }

    public void setPayload(TPayload payload) {
        this.payload = payload;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
