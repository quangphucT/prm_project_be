package com.example.demo.repository;

import com.example.demo.entity.Brand;
import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Product findProductById(long id);
    List<Product> findProductByIsDeletedFalse();
    List<Product> findByBrandAndIsDeletedFalse(Brand brand);
    List<Product> findByCategoryAndIsDeletedFalse(Category category);

}
