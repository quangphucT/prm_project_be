package com.example.demo.api;

import com.example.demo.entity.Color;
import com.example.demo.entity.Size;
import com.example.demo.model.SizeCreateRequest;
import com.example.demo.service.SizeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RequestMapping("/api/size")
@SecurityRequirement(name = "api")
@RestController
public class SizeAPI {
    @Autowired
    SizeService sizeService;
    @PostMapping
    public ResponseEntity createSize(@RequestBody SizeCreateRequest sizeCreateRequest){
        Size size = sizeService.createSize(sizeCreateRequest);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Size created successfully");
        response.put("data", size);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/all")
    public ResponseEntity getAllSize(){
         List<Size> size = sizeService.getAllSize();
         Map<String, Object> response = new LinkedHashMap<>();
         response.put("message", "Size list retrieved successfully");
         response.put("data", size);
         return ResponseEntity.ok(response);
    }
}
