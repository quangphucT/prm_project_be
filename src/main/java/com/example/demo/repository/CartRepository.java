package com.example.demo.repository;

import com.example.demo.entity.Account;
import com.example.demo.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findCartById(long id);
    Optional<Cart> findByAccount(Account account);
}
