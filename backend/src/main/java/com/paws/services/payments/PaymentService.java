package com.paws.services.payments;

import com.paws.entities.Bill;
import com.paws.payloads.response.BillDto;

import java.math.BigDecimal;

public interface PaymentService {
    String createPaymentUrl(BillDto bill);
}
