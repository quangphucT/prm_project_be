package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    Date createdAt;
    float total;
    @ManyToOne
    @JoinColumn(name = "customer_id")
            @JsonIgnore
    Account customer;


    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)
    List<OrderDetails> orderDetails;

}
