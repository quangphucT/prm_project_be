package com.example.demo.repository;

import com.example.demo.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    List<Brand> findBrandsByIsDeletedFalse();
    Brand findBrandById(Long id);

}
