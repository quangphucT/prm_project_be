package com.example.demo.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OrderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    float price;
    int quantity;

    @ManyToOne
    @JoinColumn(name = "orders_id")
            @JsonIgnore
    Orders orders;

    @ManyToOne
    @JoinColumn(name = "product_id")

    Product product;

}
