package com.Ecommerce.demo.repository;

import com.Ecommerce.demo.model.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<UsersEntity,Integer> {
}
