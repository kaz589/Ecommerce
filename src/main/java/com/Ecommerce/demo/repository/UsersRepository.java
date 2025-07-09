package com.Ecommerce.demo.repository;

import com.Ecommerce.demo.model.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<UsersEntity,Integer> {

    Optional<UsersEntity> findByUsername(String username);

    Optional<UsersEntity> findByEmail(String username);
}
