package com.example.demo.model;

import com.example.demo.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccResponseAfterLogin {
    String message;
    long id;
    String firstName;
    String lastName;
    String email;
    String phone;
    String avatar;
    Date createdAt;
    Role role;
    String token;
}
