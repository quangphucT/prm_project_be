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
public class AccountResponse {
    long id;
    String firstName;
    String lastName;
    String email;
    String phone;
    String avatar;
    Role role;
    Date createdAt;
}
