package com.example.demo.api;

import com.example.demo.entity.Product;
import com.example.demo.model.CreateProductRequest;
import com.example.demo.service.ProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@SecurityRequirement(name = "api")
@RequestMapping("/api/")
public class ProductAPI {
    @Autowired
    ProductService productService;
    @PostMapping("product")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity createNewProduct(@Valid @RequestBody CreateProductRequest createProductRequest){
        Product newProduct = productService.createNewProduct(createProductRequest);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Product created Successfully");
        response.put("product", newProduct);
        return ResponseEntity.ok(response);
    }
    @GetMapping("products")
    public ResponseEntity getAllProducts(){
        List<Product> products = productService.getAllProducts();
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("data", products);
        return ResponseEntity.ok(response);
    }
    @PutMapping("/product/{id}")
    public ResponseEntity updateProduct(@Valid @RequestBody CreateProductRequest createProductRequest, @PathVariable long id){
         Product updatedproduct = productService.updateProduct(createProductRequest, id);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Product updated Successfully");
        response.put("product", updatedproduct);
         return ResponseEntity.ok(response);
    }
    @DeleteMapping("/product/{id}")
    public ResponseEntity deleteProduct(@PathVariable long id){
        productService.deleteProduct(id);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Product deleted Successfully");
        return ResponseEntity.ok(response);
    }
    @GetMapping("/product/{id}")
    public ResponseEntity getProductById(@PathVariable long id){
        Product product = productService.getProduct(id);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("data", product);
        return ResponseEntity.ok().body(response);
    }
}
