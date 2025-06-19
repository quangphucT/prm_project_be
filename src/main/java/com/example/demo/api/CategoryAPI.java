package com.example.demo.api;

import com.example.demo.entity.Category;
import com.example.demo.model.CreateCategoryRequest;
import com.example.demo.service.CategoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/")
@SecurityRequirement(name = "api")
public class CategoryAPI {
    @Autowired
    CategoryService categoryService;

    @PostMapping("category")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity createNewCategory(@Valid @RequestBody CreateCategoryRequest createCategoryRequest){
        Category newCategory = categoryService.createCategory(createCategoryRequest);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Category created successfully");
        response.put("data", newCategory);
        return ResponseEntity.ok(response);
    }
    @GetMapping("categories")
    public ResponseEntity getAllCategories(){
        List<Category> categories = categoryService.getAllCategories();
        Map<String, Object> response = new HashMap<>();
        response.put("data", categories);
        return ResponseEntity.ok(response);
    }
    @GetMapping("category/{id}")
    public ResponseEntity getCategoryById(@PathVariable long id){
        Category category = categoryService.getCategoryById(id);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("data", category);
        return ResponseEntity.ok(response);
    }
    @PutMapping("category/{id}")
    public ResponseEntity updateCategory(@PathVariable long id, @Valid @RequestBody CreateCategoryRequest createCategoryRequest){
              Category updatedCategory = categoryService.updateCategory(id, createCategoryRequest);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Update category successfully");
        response.put("data", updatedCategory);


              return ResponseEntity.ok(response);
    }
    @DeleteMapping("category/{id}")
    public ResponseEntity deleteCategory(@PathVariable long id){
        categoryService.deleteCategory(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Deleted Category successfully");
        return ResponseEntity.ok(response);
    }
    @PutMapping("category/restore/{id}")
    public ResponseEntity restoreCategory(@PathVariable long id){
        categoryService.restoreCategory(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Restored Category successfully");
        return ResponseEntity.ok(response);
    }
}
