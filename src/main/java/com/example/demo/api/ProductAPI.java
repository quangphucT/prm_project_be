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

import java.util.List;

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
        return ResponseEntity.ok().body(newProduct);
    }
    @GetMapping("products")
    public ResponseEntity getAllProducts(){
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok().body(products);
    }
    @PutMapping("/product/{id}")
    public ResponseEntity updateProduct(@Valid @RequestBody CreateProductRequest createProductRequest, @PathVariable long id){
         Product updatedproduct = productService.updateProduct(createProductRequest, id);
         return ResponseEntity.ok().body(updatedproduct);
    }
    @DeleteMapping("/product/{id}")
    public ResponseEntity deleteProduct(@PathVariable long id){
        productService.deleteProduct(id);
        return ResponseEntity.ok("Deleted successfully");
    }
    @GetMapping("/product/{id}")
    public ResponseEntity getProductById(@PathVariable long id){
        Product product = productService.getProduct(id);
        return ResponseEntity.ok().body(product);
    }
}
