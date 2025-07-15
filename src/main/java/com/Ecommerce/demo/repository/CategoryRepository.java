package com.Ecommerce.demo.repository;

import com.Ecommerce.demo.model.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository  extends JpaRepository<CategoryEntity,Integer> {
    @Query("SELECT DISTINCT a.name FROM CategoryEntity a WHERE a.name IS NOT NULL")
    List<String> findDistinctBynameNotNull();
}
