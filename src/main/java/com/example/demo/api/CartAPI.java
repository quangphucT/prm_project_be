package com.example.demo.api;

import com.example.demo.entity.Cart;
import com.example.demo.model.AddToCart;
import com.example.demo.model.ItemCart;
import com.example.demo.service.CartService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/cart")
@SecurityRequirement(name = "api")
public class CartAPI {
    @Autowired
    CartService cartService;
    @PostMapping
    public ResponseEntity addCart(@RequestBody AddToCart addToCart) {
        Cart cart = cartService.addCart(addToCart);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Added cart successfully");
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity deleteCart(@PathVariable long id) {
             cartService.removeCart(id);
             Map<String, Object> response = new LinkedHashMap<>();
             response.put("message", "Cart successfully deleted");
             return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity getCart() {
           Cart cart = cartService.getCart();
           Map<String, Object> response = new LinkedHashMap<>();
           response.put("Cart", cart);
           return ResponseEntity.ok(response);

    }
    @PutMapping("/{id}")
    public ResponseEntity updateCartItem(@RequestParam long id, @RequestBody ItemCart ItemCart) {
             cartService.updateItemCart(id, ItemCart);
             Map<String, Object> response = new LinkedHashMap<>();
             response.put("message", "Cart successfully updated");
             return ResponseEntity.ok(response);
    }
}
