package com.example.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class PayOSService {

    @Value("${payos.client-id}")
    private String clientId;

    @Value("${payos.api-key}")
    private String apiKey;

    @Value("${payos.base-url}")
    private String baseUrl;

    @Value("${payos.return-url}")
    private String returnUrl;

    public String createPayment(UUID orderId, int amount, String description) throws JsonProcessingException {
        // ✅ Dùng timestamp để đảm bảo orderCode duy nhất
        int orderCode = (int) (System.currentTimeMillis() / 1000);

        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> body = new HashMap<>();
        body.put("orderCode", orderCode);
        body.put("amount", amount);
        body.put("description", description);
        body.put("buyerName", "Nguyen Van A");
        body.put("buyerEmail", "buyer@gmail.com");
        body.put("buyerPhone", "0901234567");
        body.put("buyerAddress", "123 Đường ABC, Quận 1, TP.HCM");

        // ✅ Tạo danh sách sản phẩm
        List<Map<String, Object>> items = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();
        item.put("name", "Gói khám HIV");
        item.put("quantity", 1);
        item.put("price", amount);
        items.add(item);
        body.put("items", items);

        body.put("returnUrl", returnUrl);
        body.put("cancelUrl", returnUrl + "?cancel=true");

        // ✅ expiredAt sau 10 phút
        long expiredAt = Instant.now().plus(10, ChronoUnit.MINUTES).getEpochSecond();
        body.put("expiredAt", expiredAt);

        // ✅ Headers đúng định dạng
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-client-id", clientId);
        headers.set("x-api-key", apiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        // ✅ In JSON gửi đi để kiểm tra
        System.out.println("GỬI LÊN PAYOS BODY:");
        System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(body));

        // ✅ Gửi request
        ResponseEntity<Map> response = restTemplate.postForEntity(
                baseUrl + "/v2/payment-requests", entity, Map.class
        );

        System.out.println("Raw PayOS response: " + response.getBody());

        // ✅ Xử lý kết quả trả về
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Object dataObj = response.getBody().get("data");

            if (dataObj instanceof Map) {
                Map<String, Object> data = (Map<String, Object>) dataObj;
                return (String) data.get("checkoutUrl");
            } else {
                throw new RuntimeException("PayOS trả về 'data' null hoặc không hợp lệ\n" + response.getBody());
            }
        }

        throw new RuntimeException("Không thể tạo thanh toán PayOS\n" + response.getBody());
    }
}
