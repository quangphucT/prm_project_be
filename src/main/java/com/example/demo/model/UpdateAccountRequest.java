package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAccountRequest {
    @NotBlank(message = "Firstname must not be blank!")
    String firstName;

    @NotBlank(message = "Lastname must bot be blank!")
    String lastName;


    @NotBlank(message = "Phone must not be blank!")
    @Pattern(regexp = "(84|0[3|5|7|8|9])+([0-9]{8})\\b", message = "Phone is invalid!")
    @Column(unique = true)
    String phone;


    String avatar;
}
