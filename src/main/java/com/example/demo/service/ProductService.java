package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.exception.DuplicationException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.CreateProductRequest;
import com.example.demo.model.ProductVariantRequest;
import com.example.demo.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    @Autowired
    BrandRepository brandRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ColorRepository colorRepository;
    @Autowired
    SizeRepository sizeRepository;
    @Autowired
    ProductVariantsRepository productVariantsRepository;

    public Product createNewProduct(CreateProductRequest createProductRequest) {
        // Check trùng tên sản phẩm
        if (productRepository.existsByNameAndIsDeletedFalse(createProductRequest.getName())) {
            throw new DuplicationException("Product name already exists!");
        }

        // Check brand & category tồn tại
        Brand brand = brandRepository.findById(createProductRequest.getBrandId())
                .orElseThrow(() -> new NotFoundException("Brand not found"));

        Category category = categoryRepository.findById(createProductRequest.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found"));

        // Check và gom các variant hợp lệ
        List<ProductVariants> variantList = new ArrayList<>();
        for (ProductVariantRequest variantReq : createProductRequest.getVariants()) {
            Color color = colorRepository.findById(variantReq.getColorId())
                    .orElseThrow(() -> new NotFoundException("Color not found"));

            Size size = sizeRepository.findById(variantReq.getSizeId())
                    .orElseThrow(() -> new NotFoundException("Size not found"));

            ProductVariants variant = new ProductVariants();
            variant.setColor(color);
            variant.setSize(size);
            variant.setStock(variantReq.getStock());
            variantList.add(variant);
        }

        // Tạo đối tượng product khi mọi thứ đã OK
        Product product = new Product();
        product.setName(createProductRequest.getName());
        product.setPrice(createProductRequest.getPrice());
        product.setImageUrl(createProductRequest.getImageUrl());
        product.setDescription(createProductRequest.getDescription());
        product.setCreatedAt(new Date());
        product.setBrand(brand);
        product.setCategory(category);
        product.setAccount(authenticationService.getCurrentAccount());

        // Lưu product
        Product savedProduct = productRepository.save(product);

        // Gán product vào từng variant và lưu
        for (ProductVariants variant : variantList) {
            variant.setProduct(savedProduct);
        }
        productVariantsRepository.saveAll(variantList);
        savedProduct.setProductVariants(variantList);

        return savedProduct;
    }


    public List<Product> getAllProducts(){
        List<Product> products = productRepository.findAllActiveProductsWithActiveVariants();
        for (Product p : products) {
            List<ProductVariants> filteredVariants = p.getProductVariants()
                    .stream()
                    .filter(v -> !Boolean.TRUE.equals(v.getIsDeleted()))
                    .toList();
            p.setProductVariants(filteredVariants);
        }
        return products;
    }

    public Product updateProduct(CreateProductRequest createProductRequest, long id) {
        Product product = productRepository.findById((int) id)
                .filter(p -> !Boolean.TRUE.equals(p.getIsDeleted()))
                .orElseThrow(() -> new NotFoundException("Product not found!"));

        // Kiểm tra trùng tên
        Product existingProduct = productRepository.findByName(createProductRequest.getName());
        if (existingProduct != null && existingProduct.getId() != id ) {
            throw new DuplicationException("Product name already exists!");
        }

        // Cập nhật thông tin cơ bản
        product.setName(createProductRequest.getName());
        product.setPrice(createProductRequest.getPrice());
        product.setImageUrl(createProductRequest.getImageUrl());
        product.setDescription(createProductRequest.getDescription());

        // Cập nhật brand và category nếu cần
        Brand brand = brandRepository.findById(createProductRequest.getBrandId())
                .orElseThrow(() -> new NotFoundException("Brand not found"));
        product.setBrand(brand);

        Category category = categoryRepository.findById(createProductRequest.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found"));
        product.setCategory(category);

        // Xoá productVariants cũ
        // "Xóa mềm" các variants cũ
        if (product.getProductVariants() != null) {
            for (ProductVariants variant : product.getProductVariants()) {
                variant.setIsDeleted(true);
            }
            productVariantsRepository.saveAll(product.getProductVariants());
        }


        // Tạo productVariants mới
        List<ProductVariants> newVariants = new ArrayList<>();
        for (ProductVariantRequest variantReq : createProductRequest.getVariants()) {
            ProductVariants variant = new ProductVariants();
            variant.setProduct(product);
            variant.setColor(colorRepository.findById(variantReq.getColorId())
                    .orElseThrow(() -> new NotFoundException("Color not found")));
            variant.setSize(sizeRepository.findById(variantReq.getSizeId())
                    .orElseThrow(() -> new NotFoundException("Size not found")));
            variant.setStock(variantReq.getStock());
            newVariants.add(variant);
        }

        // Lưu variants mới
        productVariantsRepository.saveAll(newVariants);
        product.setProductVariants(newVariants);

        return productRepository.save(product);
    }

    public void deleteProduct(long id){
        Product product = productRepository.findById((int) id).filter(b -> !b.getIsDeleted())
                .orElseThrow(() -> new NotFoundException("Product not found"));

        product.setIsDeleted(true);

        if (product.getProductVariants() != null) {
            for (ProductVariants variant : product.getProductVariants()) {
                variant.setIsDeleted(true);  // cần thêm field isDeleted trong ProductVariants entity
            }
        }

        productRepository.save(product);
    }
    public Product getProduct(long id) {
        Product product = productRepository.findProductById(id);

        if (product == null || Boolean.TRUE.equals(product.getIsDeleted())) {
            throw new NotFoundException("Product not found!");
        }

        // Lọc productVariants có isDeleted = false
        List<ProductVariants> activeVariants = product.getProductVariants()
                .stream()
                .filter(variant -> !Boolean.TRUE.equals(variant.getIsDeleted()))
                .toList();
        product.setProductVariants(activeVariants);

        return product;
    }

}
