package com.example.demo.repository;

import com.example.demo.entity.ProductVariants;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductVariantsRepository extends JpaRepository<ProductVariants, Long> {
    Optional<ProductVariants> findFirstByProductIdAndColorIdAndSizeIdAndIsDeletedFalse(
            Long productId, Long colorId, Long sizeId);

}
