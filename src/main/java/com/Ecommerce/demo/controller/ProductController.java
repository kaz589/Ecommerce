package com.Ecommerce.demo.controller;

import com.Ecommerce.demo.model.entity.ProductEntity;
import com.Ecommerce.demo.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.Ecommerce.demo.utils.SecurityUtils.getAuthenticatedUsername;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/All")
    public Page<ProductEntity> findAllProduct() {
        return productService.findAllProduct(0, 10);
    }

    //@PreAuthorize("hasRole('ROLE_Admin')")
    @PostMapping("/create")
    public ResponseEntity<ProductEntity> CreateProduct(@RequestBody ProductEntity product) {

        ProductEntity newproduct = productService.CreateProduct(product);


        return new ResponseEntity<>(newproduct, HttpStatus.OK);
    }

    @PostMapping("/purchase")
    public ResponseEntity<Void> purchase(@RequestBody Map<String, Object> data) {

        int productId = (Integer) data.get("productId");
        int quantity = (Integer) data.get("quantity");


        String username = getAuthenticatedUsername();


        productService.purchase(username, productId, quantity);


        // 如果操作成功，返回 200 OK
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

