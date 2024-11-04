package com.example.gticslab.controller;

import com.example.gticslab.entity.Category;
import com.example.gticslab.repository.CategoryRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Integer id) {
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        if (categoryOpt.isPresent()) {
            return new ResponseEntity<>(categoryOpt.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Categoría no encontrada", HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
    @PostMapping
    public ResponseEntity<String> createCategory(@RequestBody @Valid Category category) {
        Optional<Category> existingCategory = categoryRepository.findByName(category.getName());
        if (existingCategory.isPresent()) {
            return new ResponseEntity<>("El nombre de la categoría ya existe", HttpStatus.BAD_REQUEST);
        }

        categoryRepository.save(category);
        return new ResponseEntity<>("Categoría creada con éxito", HttpStatus.CREATED);
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable Integer id, @RequestBody @Valid Category categoryDetails) {
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        if (!categoryOpt.isPresent()) {
            return new ResponseEntity<>("Categoría no encontrada", HttpStatus.NOT_FOUND);
        }

        Category category = categoryOpt.get();

        Optional<Category> existingCategory = categoryRepository.findByName(categoryDetails.getName());
        if (existingCategory.isPresent() && !existingCategory.get().getId().equals(id)) {
            return new ResponseEntity<>("El nombre de la categoría ya existe", HttpStatus.BAD_REQUEST);
        }

        category.setName(categoryDetails.getName());
        categoryRepository.save(category);
        return new ResponseEntity<>("Categoría actualizada con éxito", HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Integer id) {
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        if (!categoryOpt.isPresent()) {
            return new ResponseEntity<>("Categoría no encontrada", HttpStatus.NOT_FOUND);
        }
        Category category = categoryOpt.get();

        if (!category.getEvents().isEmpty()) {
            return new ResponseEntity<>("No se puede eliminar la categoría porque tiene eventos asociados", HttpStatus.BAD_REQUEST);
        }

        categoryRepository.deleteById(id);
        return new ResponseEntity<>("Categoría eliminada con éxito", HttpStatus.OK);
    }
}
