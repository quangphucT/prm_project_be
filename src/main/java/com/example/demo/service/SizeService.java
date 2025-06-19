package com.example.demo.service;

import com.example.demo.entity.Size;
import com.example.demo.exception.DuplicationException;
import com.example.demo.model.SizeCreateRequest;
import com.example.demo.repository.SizeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SizeService {
    @Autowired
    SizeRepository sizeRepository;
    @Autowired
    ModelMapper modelMapper;
    public Size createSize(SizeCreateRequest sizeCreateRequest){
        Size size = modelMapper.map(sizeCreateRequest, Size.class);

        if (size.getNumber() < 38 || size.getNumber() > 45) {
            throw new IllegalArgumentException("Size must be between 38 and 45");
        }

        try {
            return sizeRepository.save(size);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicationException("Duplicated size number!!");
        }
    }
    public List<Size> getAllSize(){
        return sizeRepository.findSizesByIsDeletedFalse();
    }
}
