package com.Ecommerce.demo.service.implementations;

import com.Ecommerce.demo.exception.BusinessException;
import com.Ecommerce.demo.model.entity.ProductEntity;
import com.Ecommerce.demo.model.entity.UsersEntity;
import com.Ecommerce.demo.repository.ProductRepository;
import com.Ecommerce.demo.repository.UsersRepository;
import com.Ecommerce.demo.service.ProductService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.Optional;

@Service
public class ProductImp implements ProductService {

    private final ProductRepository productRepository;
    private final UsersRepository usersRepository;

    public ProductImp(ProductRepository productRepository,UsersRepository usersRepository) {
        this.productRepository = productRepository;
        this.usersRepository=usersRepository;
    }


    @Override
    public Optional<ProductEntity> findProductById(Integer id) {
        return Optional.empty();
    }

    @Override
    public Page<ProductEntity> findAllProduct(int page, int size) {
        Pageable pageable = PageRequest.of(0, 100, Sort.by("productId").ascending());

        return productRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public void purchase(String Username, int productId, int quantity) {



        // 確認商品是否存在


        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("商品不存在"));
        int nowStockQuantity = product.getStockQuantity();

        if (nowStockQuantity < quantity) {
            throw new RuntimeException("商品庫存不足");
        }
        product.setStockQuantity(nowStockQuantity - quantity);
        productRepository.save(product);

        //TODO:再檢查用戶跟餘額
        // 2. 扣除用戶餘額
        UsersEntity user = usersRepository.findByUsername(Username)
                .orElseThrow(() -> new BusinessException("用戶不存在"));

        int totalPrice = product.getPrice() * quantity;
        if (user.getBalance() < totalPrice) {
            throw new BusinessException("用戶餘額不足");
        }

        user.setBalance(user.getBalance()-totalPrice);
        usersRepository.save(user);
    }

    @Override
    public ProductEntity CreateProduct(ProductEntity product) {
        return productRepository.save(product);
    }
}
