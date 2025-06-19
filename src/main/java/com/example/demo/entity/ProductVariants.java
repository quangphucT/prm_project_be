package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ProductVariants {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Schema(defaultValue = "false")
    Boolean isDeleted =false;

    int stock;

     @ManyToOne
     @JoinColumn(name = "product_id")
             @JsonIgnore
    Product product;


     @ManyToOne
    @JoinColumn(name = "color_id")
    Color color;

     @ManyToOne
    @JoinColumn(name = "size_id")
    Size size;
}
