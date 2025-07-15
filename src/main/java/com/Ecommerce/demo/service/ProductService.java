package com.Ecommerce.demo.service;

import com.Ecommerce.demo.model.entity.ProductEntity;
import com.Ecommerce.demo.model.entity.UsersEntity;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface  ProductService {

    Optional<ProductEntity> findProductById(Integer id);
    Page<ProductEntity> findAllProduct(int page, int size);
    void purchase(String Username ,int productId,int quantity );
    ProductEntity CreateProduct(ProductEntity product);
}
