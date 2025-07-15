package com.Ecommerce.demo.service;

import com.Ecommerce.demo.model.entity.UsersEntity;
import org.springframework.data.domain.Page;

public interface UsersService {
    Page<UsersEntity> getAllUsers(int page, int size);
    UsersEntity getUserByUserId(int UserId);
    UsersEntity getUserByUserName(String UserName);
}
