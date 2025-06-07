package com.example.demo.service;

import com.example.demo.entity.Brand;
import com.example.demo.exception.DuplicationException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.CreateBrandRequest;
import com.example.demo.repository.BrandRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.util.Date;
import java.util.List;

@Service
public class BrandService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    BrandRepository brandRepository;
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
        try {
            return brand;
        } catch (Exception e) {
            throw new NotFoundException("Brand not found!");
        }
    }
    public void deleteBrandById(long id) {
        Brand brand = brandRepository.findBrandById(id);
        try {
            brand.setIsDeleted(true);
            brandRepository.save(brand);
        } catch (Exception e) {
            throw new NotFoundException("Brand not found!");
        }
    }
    public Brand updateBrand(long id, CreateBrandRequest createBrandRequest) {
        Brand brand = brandRepository.findBrandById(id);
        try {
            brand.setName(createBrandRequest.getName());
            return brandRepository.save(brand);
        } catch (Exception e) {
            throw new NotFoundException("Brand not found!");
        }
    }
}
