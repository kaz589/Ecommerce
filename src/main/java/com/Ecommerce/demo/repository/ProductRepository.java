package com.Ecommerce.demo.repository;

import com.Ecommerce.demo.model.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity,Integer> {

}
