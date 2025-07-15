package com.Ecommerce.demo.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Product")
public class ProductEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productId", nullable = false)
    private Integer productId;

    @Column(name = "name", nullable = false, length = 255, columnDefinition = "nvarchar(255) COLLATE Chinese_Taiwan_Stroke_CI_AS")
    private String name;

    @Column(name = "description", columnDefinition = "nvarchar(MAX) COLLATE Chinese_Taiwan_Stroke_CI_AS")
    private String description;

    @Column(name = "categoryId", nullable = false)
    private Integer categoryId;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private Integer price;

    @Column(name = "stockQuantity", nullable = false)
    private Integer stockQuantity;

    @Column(name = "status", length = 8, columnDefinition = "char(8) COLLATE Chinese_Taiwan_Stroke_CI_AS")
    private String status;

    @Column(name = "createdAt", columnDefinition = "datetime DEFAULT getdate()")
    private LocalDateTime createdAt;

    @Column(name = "updatedAt", columnDefinition = "datetime DEFAULT getdate()")
    private LocalDateTime updatedAt;
}
