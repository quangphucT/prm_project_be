package com.example.demo.api;

import com.example.demo.entity.Color;
import com.example.demo.model.CreateColorRequest;
import com.example.demo.service.ColorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/color")
@SecurityRequirement(name = "api")
public class ColorAPI {
    @Autowired
    ColorService colorService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity createColor(@RequestBody CreateColorRequest createColorRequest) {
          Color color = colorService.createColor(createColorRequest);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Color created successfully");
        response.put("data", color);
          return ResponseEntity.ok(response);
    }
    @GetMapping("/all")
    public ResponseEntity getAllColors() {
        List<Color> colors = colorService.getAllColors();
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("data", colors);
        return ResponseEntity.ok(response);
    }
    @PutMapping("/{id}")
    public ResponseEntity updateColor(@PathVariable long id, @RequestBody CreateColorRequest createColorRequest) {
        Color color = colorService.updateColor(id, createColorRequest);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Color updated successfully");
        response.put("data", color);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity deleteColor(@PathVariable long id) {
         colorService.deleteColor(id);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Color successfully deleted");
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity getColor(@PathVariable long id) {
       Color color =  colorService.getColorById(id);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("data", color);
       return ResponseEntity.ok(response);
    }
    @PutMapping("/restore/{id}")
    public ResponseEntity restoreColor(@PathVariable long id) {
        colorService.restoreColor(id);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Color successfully restored");
        return ResponseEntity.ok(response);
    }
}
