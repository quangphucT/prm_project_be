package com.example.demo.service;

import com.example.demo.entity.Brand;
import com.example.demo.entity.Product;
import com.example.demo.exception.DuplicationException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.CreateBrandRequest;
import com.example.demo.repository.BrandRepository;
import com.example.demo.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BrandService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    BrandRepository brandRepository;

    @Autowired
    ProductRepository productRepository;
    public Brand createBrand(CreateBrandRequest createBrandRequest) {
        Brand brand = modelMapper.map(createBrandRequest, Brand.class);
        try {
            brand.setCreatedAt(new Date());
            return brandRepository.save(brand);
        }catch (Exception e) {
            throw new DuplicationException("Duplicated brand name!");
        }
    }
    public List<Brand> getAllBrands() {
        return brandRepository.findBrandsByIsDeletedFalse();
    }
    public Brand getBrandById(long id) {
        Brand brand = brandRepository.findBrandById(id);
        if (brand == null) {
            throw new NotFoundException("Brand not found!");
        }else{
            return brand;
        }
    }
    public void deleteBrandById(long id) {
        Brand brand = brandRepository.findBrandById(id);
        if(brand == null || brand.getIsDeleted()){
            throw new NotFoundException("Brand not found!");
        }
        List<Product> activeProducts = productRepository.findByBrandAndIsDeletedFalse(brand);
        if(!activeProducts.isEmpty()){
            throw new IllegalStateException("Can not delete this brand due to having active products!");
        }

            brand.setIsDeleted(true);
            brandRepository.save(brand);
    }
    public Brand updateBrand(long id, CreateBrandRequest createBrandRequest) {
        Brand brand = brandRepository.findById(id)
                .filter(b -> !b.getIsDeleted()) // ✅ chỉ lấy brand chưa bị xóa
                .orElseThrow(() -> new NotFoundException("Brand not found"));

        if (brandRepository.existsByNameAndIdNot(createBrandRequest.getName(), id)) {
            throw new DuplicationException("Brand name already exists");
        }

        brand.setName(createBrandRequest.getName());
        return brandRepository.save(brand);
    }

    public void restoreBrand(long id) {
        Brand brand = brandRepository.findBrandById(id);
        if(brand == null || !brand.getIsDeleted()){
            throw new NotFoundException("Brand not found!");
        }else{
            brand.setIsDeleted(false);
            brandRepository.save(brand);
        }
    }
}
