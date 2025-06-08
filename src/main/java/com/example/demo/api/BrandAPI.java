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

import java.util.List;

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
        return ResponseEntity.ok(newBrand);
    }
    @GetMapping("brands")
    public ResponseEntity getAllBrands(){
        List<Brand> allBrands = brandService.getAllBrands();
        return ResponseEntity.ok(allBrands);
    }
    @GetMapping("brand/{id}")
    public ResponseEntity getBrandById(@PathVariable long id){
        Brand brand = brandService.getBrandById(id);
        return ResponseEntity.ok(brand);
    }
    @DeleteMapping("brand/{id}")
    public ResponseEntity deleteBrandById(@PathVariable long id){
        brandService.deleteBrandById(id);
        return ResponseEntity.ok("Deleted Brand Successfully");
    }
    @PutMapping("brand/{id}")
    public ResponseEntity updateBrandById(@PathVariable long id, @Valid @RequestBody CreateBrandRequest createBrandRequest){
        Brand updateBrand = brandService.updateBrand(id, createBrandRequest);
        return ResponseEntity.ok(updateBrand);
    }
    @PutMapping("brand/restore/{id}")
    public ResponseEntity restoreBrandById(@PathVariable long id){
        brandService.restoreBrand(id);
        return ResponseEntity.ok("Restored Brand Successfully");
    }
}
