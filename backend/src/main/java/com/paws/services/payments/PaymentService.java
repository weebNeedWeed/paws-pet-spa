package com.paws.services.payments;

import com.paws.exceptions.InvalidPaymentResultException;
import com.paws.payloads.request.ValidatePaymentResultRequest;
import com.paws.payloads.response.BillDto;

public interface PaymentService {
    String createPaymentUrl(BillDto bill);

    void validatePaymentResult(ValidatePaymentResultRequest request) throws InvalidPaymentResultException;
}
