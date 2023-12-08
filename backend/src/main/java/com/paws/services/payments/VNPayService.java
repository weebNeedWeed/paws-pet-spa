package com.paws.services.payments;

import com.paws.entities.Bill;
import com.paws.payloads.response.BillDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
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
