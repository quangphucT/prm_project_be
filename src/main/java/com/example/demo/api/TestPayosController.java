package com.example.demo.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.HttpURLConnection;
import java.net.URL;

@RestController
@SecurityRequirement(name = "api")
@RequestMapping("/api/test-payos")
public class TestPayosController {
    @GetMapping
    public ResponseEntity<String> testPayos() {
        try {
            URL url = new URL("https://api.payos.vn/v2/payment-requests");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            int responseCode = con.getResponseCode();
            return ResponseEntity.ok("PayOS response code: " + responseCode);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}
