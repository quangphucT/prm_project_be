package com.example.demo.repository;

import com.example.demo.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ColorRepository extends JpaRepository<Color, Long> {
    List<Color> findColorsByIsDeletedFalse();
    Color findColorById(Long id);
    boolean existsByNameAndIdNot(String name, long id);
}
