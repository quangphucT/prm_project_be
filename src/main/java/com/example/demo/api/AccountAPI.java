package com.example.demo.api;
import com.example.demo.model.AccountResponse;
import com.example.demo.model.UpdateAccountRequest;
import com.example.demo.service.AccountService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/")
@SecurityRequirement(name = "api")
public class AccountAPI {
    @Autowired
    AccountService accountService;

    @GetMapping("account/{id}")
        public ResponseEntity getAccount(@PathVariable long id) {
        AccountResponse detailsAccResponse = accountService.getAccountById(id);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("data", detailsAccResponse);
        return ResponseEntity.ok(response);
    }
    @PutMapping("account/{id}")
    public ResponseEntity updateAccount(@PathVariable long id, @Valid @RequestBody UpdateAccountRequest updateAccountRequest) {
        AccountResponse detailsAccResponse = accountService.updateAccount(id, updateAccountRequest);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Updated successfully");
        response.put("data", detailsAccResponse);
        return ResponseEntity.ok(response);
    }
}
