package com.example.demo.api;

import com.example.demo.entity.Orders;
import com.example.demo.model.OrderRequest;
import com.example.demo.service.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RequestMapping("/api/")
@SecurityRequirement(name = "api")
@RestController
public class OrderAPI {

    @Autowired
    OrderService orderService;
    @PostMapping("order")
    public ResponseEntity createOrder(@Valid @RequestBody OrderRequest orderRequest){
         Orders order = orderService.createOrder(orderRequest);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("mesage", "Order created successfully");
        response.put("data", order);
         return ResponseEntity.ok(response);
    }
    @GetMapping("orders")
    public ResponseEntity getOrders(){
        List<Orders> orders = orderService.getOrders();
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("orders", orders);
        return ResponseEntity.ok(response);
    }
}
