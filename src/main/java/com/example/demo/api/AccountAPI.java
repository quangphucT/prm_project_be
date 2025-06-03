package com.example.demo.api;
import com.example.demo.model.AccountResponse;
import com.example.demo.model.UpdateAccountRequest;
import com.example.demo.service.AccountService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/")
@SecurityRequirement(name = "api")
public class AccountAPI {
    @Autowired
    AccountService accountService;
    @GetMapping("account/{id}")
        public AccountResponse getAccount(@PathVariable long id) {
        AccountResponse detailsAccResponse = accountService.getAccountById(id);
        return detailsAccResponse;
    }
    @PutMapping("account/{id}")
    public AccountResponse updateAccount(@PathVariable long id, @Valid @RequestBody UpdateAccountRequest updateAccountRequest) {
        AccountResponse detailsAccResponse = accountService.updateAccount(id, updateAccountRequest);
        return detailsAccResponse;
    }
}
