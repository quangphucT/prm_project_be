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
import java.util.List;

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
    String name;

    @Min(value = 10000, message = "Price must be at least 10000!")
    float price;





    @NotBlank(message = "ImageUrl must not be blank!")
    String imageUrl;

    @Lob
    @NotBlank(message = "Description must not be blank!")
    String description;



    Date createdAt;


    @ManyToOne
    @JoinColumn(name = "brand_id")
    Brand brand;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;

    @ManyToOne
    @JoinColumn(name = "account_id")
            @JsonIgnore
    Account account;


    @OneToMany(mappedBy = "product")
            @JsonIgnore
    List<OrderDetails> orderDetails;





    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)

     List<ProductVariants> productVariants;



    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
            @JsonIgnore
    List<CartItem> cartItems;
}
