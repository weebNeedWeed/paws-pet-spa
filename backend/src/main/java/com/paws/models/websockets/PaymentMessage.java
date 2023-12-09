package com.paws.models.websockets;

import com.paws.payloads.response.BillDto;

public class PaymentMessage {
    private String paymentUrl;
    private BillDto bill;

    public PaymentMessage() {
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public BillDto getBill() {
        return bill;
    }

    public void setBill(BillDto bill) {
        this.bill = bill;
    }
}
