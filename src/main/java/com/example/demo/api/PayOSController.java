package com.example.demo.api;

import com.example.demo.model.PayRequest;
import com.example.demo.service.PayOSService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin("*")
@SecurityRequirement(name = "api")
@RequestMapping("/api/pay")
public class PayOSController {

    @Autowired
    private PayOSService payOSService;

    @PostMapping
    public ResponseEntity<?> pay(@RequestBody PayRequest request) throws JsonProcessingException {
        String checkoutUrl = payOSService.createPayment(
                request.getOrderId(),
                request.getAmount(),
                request.getDescription()
        );

        return ResponseEntity.ok(Map.of("checkoutUrl", checkoutUrl));
    }

}
