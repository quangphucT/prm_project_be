package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SizeCreateRequest {
    @Min(value = 38, message = "Size number must be at least 38!")
    @Max(value = 45, message = "Size number must not be larger than 45!")
    @Column(unique = true)
    int number;
}
