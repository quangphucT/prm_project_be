package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCategoryRequest {
    @NotBlank(message = "Name must not be blank!")
    @Column(unique = true)
    String name;

    @NotBlank(message = "Description must not be blank!")
    String description;
}
