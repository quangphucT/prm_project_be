package com.example.demo.api;

import com.example.demo.model.AccResponseAfterLogin;
import com.example.demo.model.AccountResponse;
import com.example.demo.model.LoginRequest;
import com.example.demo.model.RegisterRequest;
import com.example.demo.service.AuthenticationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.ok(newAccount);
    }
    @PostMapping("login")
    public ResponseEntity login(@Valid @RequestBody LoginRequest loginRequest) {
        AccResponseAfterLogin accountResponseAfterLogin = authenticationService.login(loginRequest);
        return ResponseEntity.ok(accountResponseAfterLogin);
    }
}
