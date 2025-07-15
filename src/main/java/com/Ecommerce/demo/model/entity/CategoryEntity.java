package com.Ecommerce.demo.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Category")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false)
    private Integer categoryId;

    @Column(name = "name", nullable = false, length = 255, columnDefinition = "nvarchar(255) COLLATE Chinese_Taiwan_Stroke_CI_AS")
    private String name;

    @Column(name = "created_at", columnDefinition = "datetime DEFAULT getdate()")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "datetime DEFAULT getdate()")
    private LocalDateTime updatedAt;
}
