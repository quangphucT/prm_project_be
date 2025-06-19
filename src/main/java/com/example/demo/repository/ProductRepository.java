package com.example.demo.repository;

import com.example.demo.entity.Brand;
import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Product findProductById(long id);
    List<Product> findProductByIsDeletedFalse();
    List<Product> findByBrandAndIsDeletedFalse(Brand brand);
    List<Product> findByCategoryAndIsDeletedFalse(Category category);
    boolean existsByNameAndIsDeletedFalse(String name);
    Product findByName(String name);


    @Query("SELECT DISTINCT p FROM Product p JOIN p.productVariants v " +
            "WHERE p.isDeleted = false AND v.isDeleted = false")
    List<Product> findAllActiveProductsWithActiveVariants();

}
