package com.example.demo.service;

import com.example.demo.entity.Color;
import com.example.demo.exception.DuplicationException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.CreateColorRequest;
import com.example.demo.repository.ColorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColorService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    ColorRepository colorRepository;
    public Color createColor(CreateColorRequest createColorRequest) {
          Color color = modelMapper.map(createColorRequest, Color.class);
          try {
              return colorRepository.save(color);
          } catch (Exception e) {
              throw new DuplicationException("Duplicated color name!!");
          }
    }
    public List<Color> getAllColors() {
        return colorRepository.findColorsByIsDeletedFalse();
    }
    public Color updateColor(long id, CreateColorRequest createColorRequest) {
        Color color = colorRepository.findById(id)
                .filter(b -> !b.getIsDeleted()) // ✅ chỉ lấy brand chưa bị xóa
                .orElseThrow(() -> new NotFoundException("Color not found"));

        if (colorRepository.existsByNameAndIdNot(createColorRequest.getName(), id)) {
            throw new DuplicationException("Color name already exists");
        }

                 color.setName(createColorRequest.getName());
                 return colorRepository.save(color);

    }
    public void deleteColor(long id) {
        Color color = colorRepository.findColorById(id);
        if (color == null || color.getIsDeleted()) {
            throw new NotFoundException("Color not found!");
        }else{
            color.setIsDeleted(true);
            colorRepository.save(color);
        }
    }
    public Color getColorById(long id) {
        Color color = colorRepository.findColorById(id);
        if (color == null || color.getIsDeleted()) {
            throw new NotFoundException("Color not found!");
        }
        return color;
    }
    public void restoreColor(long id) {
        Color color = colorRepository.findColorById(id);
        if(color == null || !color.getIsDeleted()){
            throw new NotFoundException("Color not found!");
        }else{
            color.setIsDeleted(false);
            colorRepository.save(color);
        }
    }
}
