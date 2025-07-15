package com.Ecommerce.demo.service.implementations;

import com.Ecommerce.demo.model.entity.UsersEntity;
import com.Ecommerce.demo.repository.UsersRepository;
import com.Ecommerce.demo.service.UsersService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@Service
public class UsersImp implements UsersService {

    public final UsersRepository usersRepository;
    public UsersImp(UsersRepository usersRepository){ this.usersRepository=usersRepository;}


    @Override
    public Page<UsersEntity> getAllUsers(int page, int size) {









        Pageable pageable = PageRequest.of(0, 10, Sort.by("UserId").ascending());

        return usersRepository.findAll(pageable);


    }

    @Override
    public UsersEntity getUserByUserId(int UserId) {
        return usersRepository.findByUserId(UserId)
                .orElse(new UsersEntity());
    }

    @Override
    public UsersEntity getUserByUserName(String UserName) {
        return usersRepository.findByUsername(UserName)
                .orElse(new UsersEntity());
    }
}
