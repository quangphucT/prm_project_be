package com.example.demo.model;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductRequest {
    @NotBlank(message = "Name must not be blank!")
    String name;

    @Min(value = 100000, message = "Price must be at least 100000!")
    @Max(value = 1000000, message = "Price must not be larger than 1000000!")
    float price;


    @NotBlank(message = "ImageUrl must not be blank!")
    String imageUrl;

    @Lob
    @NotBlank(message = "Description must not be blank!")
    String description;
    long brandId;
    long categoryId;

    List<ProductVariantRequest> variants;


}
