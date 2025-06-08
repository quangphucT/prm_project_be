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

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/")
@SecurityRequirement(name = "api")
public class CategoryAPI {
    @Autowired
    CategoryService categoryService;

    @PostMapping("category")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Category createNewCategory(@Valid @RequestBody CreateCategoryRequest createCategoryRequest){
        Category newCategory = categoryService.createCategory(createCategoryRequest);
        return newCategory;
    }
    @GetMapping("categories")
    public List<Category> getAllCategories(){
        List<Category> categories = categoryService.getAllCategories();
        return categories;
    }
    @GetMapping("category/{id}")
    public Category getCategoryById(@PathVariable long id){
        Category category = categoryService.getCategoryById(id);
        return category;
    }
    @PutMapping("category/{id}")
    public Category updateCategory(@PathVariable long id, @Valid @RequestBody CreateCategoryRequest createCategoryRequest){
              Category updatedCategory = categoryService.updateCategory(id, createCategoryRequest);
              return updatedCategory;
    }
    @DeleteMapping("category/{id}")
    public ResponseEntity deleteCategory(@PathVariable long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Deleted Category successfully");
    }
    @PutMapping("category/restore/{id}")
    public ResponseEntity restoreCategory(@PathVariable long id){
        categoryService.restoreCategory(id);
        return ResponseEntity.ok("Restored Category successfully");
    }
}
