package com.example.demo.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
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
@Entity
public class Color {
      @Schema(defaultValue = "false")
      Boolean isDeleted = false;
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      long id;

      @NotBlank(message = "Name must not be blank!!")
      @Column(unique = true)
      String name;

      @ManyToMany
      @JsonIgnore
      List<Product> products;

      @OneToMany(mappedBy = "color")
              @JsonIgnore
      List<ProductVariants> productVariants;



      @OneToMany(mappedBy = "color")
              @JsonIgnore
      List<OrderDetails> orderDetails;
}
