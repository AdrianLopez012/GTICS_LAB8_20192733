package com.example.gticslab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.gticslab.entity.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findByName(String name);
}
