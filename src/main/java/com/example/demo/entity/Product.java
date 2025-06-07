package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {
    @Schema(defaultValue = "false")
    Boolean isDeleted = false;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @NotBlank(message = "Name must not be blank!")
    @Column(unique = true)
    String name;

    @Min(value = 100000, message = "Price must be at least 100000!")
    @Max(value = 1000000, message = "Price must not be larger than 1000000!")
    float price;



    @NotBlank(message = "Color must not be blank!")
    String color;

    @NotBlank(message = "ImageUrl must not be blank!")
    String imageUrl;

    @NotBlank(message = "Description must not be blank!")
    String description;

    @Min(value = 38, message = "Size must be at least 38!")
    @Max(value = 44, message = "Size must not be larger than 44!")
    float size;

    Date createdAt;


    @ManyToOne
    @JoinColumn(name = "brand_id")
    Brand brand;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;

    @ManyToOne
    @JoinColumn(name = "account_id")
    Account account;


}
