package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.Brand;
import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.exception.DuplicationException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.CreateProductRequest;
import com.example.demo.repository.BrandRepository;
import com.example.demo.repository.ProductRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    BrandService brandService;
    @Autowired
    CategoryService categoryService;
    public Product createNewProduct(CreateProductRequest createProductRequest){
        Product product = new Product();
        try {
            product.setName(createProductRequest.getName());
            product.setPrice(createProductRequest.getPrice());
            product.setColor(createProductRequest.getColor());
            product.setImageUrl(createProductRequest.getImageUrl());
            product.setDescription(createProductRequest.getDescription());
            product.setSize(createProductRequest.getSize());
            product.setCreatedAt(new Date());
            Brand brand =  brandService.getBrandById(createProductRequest.getBrandId());
            product.setBrand(brand);
            Category category = categoryService.getCategoryById(createProductRequest.getCategoryId());
            product.setCategory(category);
            Account account = authenticationService.getCurrentAccount();
            product.setAccount(account);
            return productRepository.save(product);
        } catch (Exception e) {
            throw new DuplicationException("Duplicated name!");
        }
    }
    public List<Product> getAllProducts(){
        List<Product> products = productRepository.findProductByIsDeletedFalse();
        return products;
    }
    public Product updateProduct(CreateProductRequest createProductRequest,long id){
        Product product = productRepository.findProductById(id);
        try {
            product.setName(createProductRequest.getName());
            product.setPrice(createProductRequest.getPrice());
            product.setColor(createProductRequest.getColor());
            product.setImageUrl(createProductRequest.getImageUrl());
            product.setDescription(createProductRequest.getDescription());
            product.setSize(createProductRequest.getSize());
            return productRepository.save(product);
        } catch (Exception e) {
            throw new NotFoundException("Product not found!");
        }
    }
    public void deleteProduct(long id){
        Product product = productRepository.findProductById(id);
        if(product == null){
            throw new NotFoundException("Product not found!");
        }

        if((product.getBrand() != null && !product.getBrand().getIsDeleted())
                || (product.getCategory() != null && !product.getCategory().getIsDeleted())){
            throw new IllegalStateException("Cannot delete product because it is associated with Brand or Category.");
        }
        try {
            product.setIsDeleted(true);
            productRepository.save(product);
        } catch (Exception e) {
            throw new RuntimeException("Error while deleting product!");
        }
    }
    public Product getProduct(long id){
        Product product = productRepository.findProductById(id);
        try {
            return product;
        } catch (Exception e) {
            throw new NotFoundException("Product not found!");
        }
    }
}
