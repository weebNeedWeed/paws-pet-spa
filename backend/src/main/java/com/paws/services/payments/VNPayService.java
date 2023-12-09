package com.paws.services.payments;

import com.paws.entities.Bill;
import com.paws.entities.common.enums.BillStatus;
import com.paws.exceptions.InvalidPaymentResultException;
import com.paws.payloads.request.ValidatePaymentResultRequest;
import com.paws.payloads.response.BillDto;
import com.paws.repositories.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class VNPayService implements PaymentService{
    @Value("${vnpay.secret}")
    private String secret;

    @Value("${vnpay.vnp_TmnCode}")
    private String vnp_TmnCode;

    @Value("${vnpay.vnp_Url}")
    private String vnp_Url;

    private final BillRepository billRepository;

    @Autowired
    public VNPayService(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    public String createPaymentUrl(BillDto bill) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

        params.add("vnp_Amount", bill.getTotalAmount().multiply(BigDecimal.valueOf(100)).setScale(0).toString());
        params.add("vnp_Command", "pay");
        params.add("vnp_CreateDate", formatter.format(LocalDateTime.now()));
        params.add("vnp_CurrCode", "VND");
        params.add("vnp_IpAddr", "127.0.0.1");
        params.add("vnp_Locale", "vn");
        params.add("vnp_OrderInfo", URLEncoder.encode("Thanh toan hoa don", StandardCharsets.US_ASCII));
        params.add("vnp_OrderType", "250000");
        params.add("vnp_ReturnUrl", URLEncoder.encode("http://localhost:8080/vnp_return", StandardCharsets.US_ASCII));
        params.add("vnp_TmnCode", vnp_TmnCode);
        params.add("vnp_TxnRef", bill.getId().toString());
        params.add("vnp_Version", "2.1.0");

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(vnp_Url)
                .queryParams(params);
        String hashData = builder.build().getQuery();

        builder.queryParam("vnp_SecureHash", hmacSHA512(secret, hashData));

        return builder.build().toString();
    }

    @Override
    public void validatePaymentResult(ValidatePaymentResultRequest request) throws InvalidPaymentResultException {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString("");
        uriComponentsBuilder.queryParam("vnp_Amount", request.getVnp_Amount());
        uriComponentsBuilder.queryParam("vnp_BankCode", request.getVnp_BankCode());
        uriComponentsBuilder.queryParam("vnp_BankTranNo", request.getVnp_BankTranNo());
        uriComponentsBuilder.queryParam("vnp_CardType", request.getVnp_CardType());
        uriComponentsBuilder.queryParam("vnp_OrderInfo",  URLEncoder.encode(request.getVnp_OrderInfo(), StandardCharsets.US_ASCII));
        uriComponentsBuilder.queryParam("vnp_PayDate", request.getVnp_PayDate());
        uriComponentsBuilder.queryParam("vnp_ResponseCode", request.getVnp_ResponseCode());
        uriComponentsBuilder.queryParam("vnp_TmnCode", request.getVnp_TmnCode());
        uriComponentsBuilder.queryParam("vnp_TransactionNo", request.getVnp_TransactionNo());
        uriComponentsBuilder.queryParam("vnp_TransactionStatus", request.getVnp_TransactionStatus());
        uriComponentsBuilder.queryParam("vnp_TxnRef", request.getVnp_TxnRef());

        String hashData = uriComponentsBuilder.build().getQuery();

        String hash = hmacSHA512(secret, hashData);
        if(!hash.equals(request.getVnp_SecureHash())) {
            throw new InvalidPaymentResultException("Thông tin thanh toán không hợp lệ");
        }

        // check bill exists
        Bill bill = billRepository.findById(Long.parseLong(request.getVnp_TxnRef())).orElseThrow(
                () -> new InvalidPaymentResultException("Không tìm thấy hoá đơn"));

        // check total amount;
        BigDecimal amount = request.getVnp_Amount();
        if(!amount.equals(bill.getTotalAmount().multiply(BigDecimal.valueOf(100)).setScale(0))) {
            throw new InvalidPaymentResultException("Số tiền không hợp lệ");
        }

        // check bill status
        if(bill.getStatus() != BillStatus.UN_PAID) {
            throw new InvalidPaymentResultException("Hoá đơn đã được thanh toán");
        }

        if(!request.getVnp_ResponseCode().equals("00") || !request.getVnp_TransactionStatus().equals("00")) {
            throw new InvalidPaymentResultException("Thanh toán không thành công");
        }
    }


    private String hmacSHA512(final String key, final String data) {
        try {

            if (key == null || data == null) {
                throw new NullPointerException();
            }
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes();
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();

        } catch (Exception ex) {
            return "";
        }
    }
}
