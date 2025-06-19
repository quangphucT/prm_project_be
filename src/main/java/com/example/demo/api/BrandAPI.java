package com.example.demo.api;

import com.example.demo.entity.Brand;
import com.example.demo.entity.Product;
import com.example.demo.model.CreateBrandRequest;
import com.example.demo.service.BrandService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.OneToMany;
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
@RequestMapping("/api/")
@SecurityRequirement(name = "api")

public class BrandAPI {
    @Autowired
    BrandService brandService;
    @PostMapping("brand")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity createNewBrand(@Valid @RequestBody CreateBrandRequest createBrandRequest){
        Brand newBrand = brandService.createBrand(createBrandRequest);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Brand created successfully");
        response.put("data", newBrand);
        return ResponseEntity.ok(response);
    }
    @GetMapping("brands")
    public ResponseEntity getAllBrands(){
        List<Brand> allBrands = brandService.getAllBrands();
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("data", allBrands);
        return ResponseEntity.ok(response);
    }
    @GetMapping("brand/{id}")
    public ResponseEntity getBrandById(@PathVariable long id){
        Brand brand = brandService.getBrandById(id);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("data", brand);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("brand/{id}")
    public ResponseEntity deleteBrandById(@PathVariable long id){
        brandService.deleteBrandById(id);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Deleted Brand Successfully");
        return ResponseEntity.ok(response);
    }
    @PutMapping("brand/{id}")
    public ResponseEntity updateBrandById(@PathVariable long id, @Valid @RequestBody CreateBrandRequest createBrandRequest){
        Brand updateBrand = brandService.updateBrand(id, createBrandRequest);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Updated Brand Successfully");
        response.put("data", updateBrand);
        return ResponseEntity.ok(response);
    }
    @PutMapping("brand/restore/{id}")
    public ResponseEntity restoreBrandById(@PathVariable long id){
        brandService.restoreBrand(id);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Restored Brand Successfully");
        return ResponseEntity.ok(response);
    }
}
