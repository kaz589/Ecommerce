package com.Ecommerce.demo.service.implementations;

import com.Ecommerce.demo.exception.BusinessException;
import com.Ecommerce.demo.model.entity.ProductEntity;
import com.Ecommerce.demo.model.entity.UsersEntity;
import com.Ecommerce.demo.repository.ProductRepository;
import com.Ecommerce.demo.repository.UsersRepository;
import com.Ecommerce.demo.service.ProductService;

import com.Ecommerce.demo.service.UsersService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

@Service
public class ProductImp implements ProductService {

    private final ProductRepository productRepository;

    private final UsersService usersService;

    public ProductImp(ProductRepository productRepository, UsersService usersService) {
        this.productRepository = productRepository;

        this.usersService = usersService;
    }




    @Override
    public Optional<ProductEntity> findProductById(Integer id) {

        Optional<ProductEntity> product = productRepository.findById(id);
        if (product.isEmpty()) {
            throw new BusinessException("商品不存在");
        }
        return product;


    }

    @Override
    public Page<ProductEntity> findAllProduct(int page, int size) {
        // 建立分頁請求
        Pageable pageable = PageRequest.of(0, 10, Sort.by("productId").ascending());

        // 獲取分頁結果
        Page<ProductEntity> products = productRepository.findAll(pageable);

        // 檢查是否有產品
        if (products.hasContent()) {
            return products;
        } else {
            throw new BusinessException("查無商品");
        }
    }

    @Override
    @Transactional
    public void purchase(String username, int productId, int quantity) {
        // 1. 確認商品是否存在並檢查庫存
        ProductEntity product = validateAndFetchProduct(productId, quantity);

        // 2. 確認用戶是否存在並檢查餘額
        UsersEntity user = usersService.validateAndFetchUser(username, product.getPrice() * quantity);

        // 3. 更新商品庫存
        product.setStockQuantity(product.getStockQuantity() - quantity);
        updateProduct(product);


        // 4. 更新用戶餘額
        int amount= (product.getPrice() * quantity);
        user.setBalance(user.getBalance() - amount);
        usersService.updateUsers(user);
    }
    @Override
    public ProductEntity validateAndFetchProduct(int productId, int quantity) {



        ProductEntity product = findProductById(productId)
                .orElseThrow(() -> new BusinessException("商品不存在"));

        if (product.getStockQuantity() < quantity) {
            throw new BusinessException("商品庫存不足");
        }

        return product;
    }

    @Override
    public ProductEntity updateProduct(ProductEntity product) {
        return productRepository.save(product);
    }





    @Override
    public ProductEntity createProduct(ProductEntity product) {
        return productRepository.save(product);
    }
}
