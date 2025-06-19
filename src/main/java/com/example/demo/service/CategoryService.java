package com.example.demo.service;

import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.exception.DuplicationException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.CreateCategoryRequest;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CategoryService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ProductRepository productRepository;

    public Category createCategory(CreateCategoryRequest createCategoryRequest){
        Category category = modelMapper.map(createCategoryRequest, Category.class);
        try {
            category.setCreatedAt(new Date());
           return categoryRepository.save(category);

        } catch (Exception e) {
            throw new DuplicationException("Duplicated name!");
        }
    }
    public List<Category> getAllCategories(){
        return categoryRepository.findCategoriesByIsDeletedFalse();
    }
    public Category getCategoryById(long id){
        Category category = categoryRepository.findCategoryById(id);
        if(category == null){
            throw new NotFoundException("Category not found!");
        }else{
            return category;
        }
    }
    public Category updateCategory(long id, CreateCategoryRequest req) {
        Category category = categoryRepository.findById(id).filter(b -> !b.getIsDeleted())
                .orElseThrow(() -> new NotFoundException("Category not found"));


        if (categoryRepository.existsByNameAndIdNot(req.getName(), id)) {
            throw new DuplicationException("Category name already exists");
        }


        category.setName(req.getName());
        category.setDescription(req.getDescription());
        return categoryRepository.save(category);
    }

    public void deleteCategory(long id){

        Category category = categoryRepository.findCategoryById(id);
        if(category == null || category.getIsDeleted()){
            throw new NotFoundException("Category not found!");
        }

        List<Product> activeProducts = productRepository.findByCategoryAndIsDeletedFalse(category);
          if(!activeProducts.isEmpty()){
              throw new IllegalStateException("Can not delete this category due to having active products!");
          }

            category.setIsDeleted(true);
            categoryRepository.save(category);
    }
    public void restoreCategory(long id){
        Category category = categoryRepository.findCategoryById(id);
        if(category == null || !category.getIsDeleted()){
            throw new NotFoundException("Category not found!");
        }else{
            category.setIsDeleted(false);
            categoryRepository.save(category);
        }
    }
}
