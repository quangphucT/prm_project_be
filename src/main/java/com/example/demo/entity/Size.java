package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Size {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Schema(defaultValue = "false")
    Boolean isDeleted = false;
    @Min(value = 38, message = "Size number must be at least 38!")
    @Max(value = 45, message = "Size number must not be larger than 45!")
    @Column(unique = true)
    int number;

    @ManyToMany
            @JsonIgnore
    List<Product> products;

    @OneToMany(mappedBy = "size")
            @JsonIgnore
    List<ProductVariants> productVariants;



    @OneToMany(mappedBy = "size")
            @JsonIgnore
    List<OrderDetails> orderDetails;


    @OneToMany(mappedBy = "size")
            @JsonIgnore
    List<CartItem> cartItems;
}
