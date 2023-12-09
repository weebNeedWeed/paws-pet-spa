package com.paws.payloads.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ValidatePaymentResultRequest {
    private String vnp_TmnCode;
    private BigDecimal vnp_Amount;
    private String vnp_BankCode;
    private String vnp_OrderInfo;
    private BigDecimal vnp_TransactionNo;
    private String vnp_ResponseCode;
    private String vnp_TransactionStatus;
    private String vnp_TxnRef;
    private String vnp_SecureHash;
    private String vnp_PayDate;
    private String vnp_CardType;
    private String vnp_BankTranNo;
    private String vnp_SecureHashType;
}
