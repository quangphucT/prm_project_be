package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    int quantity;
    @ManyToOne
    @JoinColumn(name = "color_id")

    Color color;

    @ManyToOne
    @JoinColumn(name ="size_id")

    Size size;
    @ManyToOne
    @JoinColumn(name = "cart_id")
    @JsonIgnore
    Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;





}
