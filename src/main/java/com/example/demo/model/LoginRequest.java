package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @Email(message = "Email is invalid!")
    @NotBlank(message = "Email must not be blank!")
    @Column(unique = true)
    String email;

    @NotBlank(message = "Password must not be blank!")
    @Size(message = "Password must be at least 8 characters!")
    String password;
}
