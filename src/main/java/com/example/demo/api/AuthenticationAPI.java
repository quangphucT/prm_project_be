package com.example.demo.api;

import com.example.demo.model.*;
import com.example.demo.service.AuthenticationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/auth/")
@SecurityRequirement(name = "api")
public class AuthenticationAPI {
    @Autowired
    AuthenticationService authenticationService;
    @PostMapping("register")
    public ResponseEntity register(@Valid @RequestBody RegisterRequest registerRequest) {
        AccountResponse newAccount = authenticationService.register(registerRequest);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Register successfully");
        response.put("data", newAccount);
        return ResponseEntity.ok(response);
    }
    @PostMapping("login")
    public ResponseEntity login(@Valid @RequestBody LoginRequest loginRequest) {
        AccResponseAfterLogin accountResponseAfterLogin = authenticationService.login(loginRequest);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Login successfully");
        response.put("data", accountResponseAfterLogin);
        return ResponseEntity.ok(response);
    }
    @PostMapping("forgot-password")
    public ResponseEntity forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
         authenticationService.forgotPassword(forgotPasswordRequest);
         Map<String, Object> response = new LinkedHashMap<>();
         response.put("message", "Please check your email to reset your password!!");
         return ResponseEntity.ok(response);
    }
    @PostMapping("reset-password")
    public ResponseEntity  resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest ) {
             authenticationService.resetPassword(resetPasswordRequest);
             Map<String, Object> response = new LinkedHashMap<>();
             response.put("message", "Reset password successfully");
             return ResponseEntity.ok(response);
    }

}
