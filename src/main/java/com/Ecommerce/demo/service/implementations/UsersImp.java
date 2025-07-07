package com.Ecommerce.demo.service.implementations;

import com.Ecommerce.demo.model.entity.UsersEntity;
import com.Ecommerce.demo.repository.UsersRepository;
import com.Ecommerce.demo.service.UsersService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class UsersImp implements UsersService {

    public final UsersRepository usersRepository;
    public UsersImp(UsersRepository usersRepository){ this.usersRepository=usersRepository;}


    @Override
    public Page<UsersEntity> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("playerId").ascending());

        return usersRepository.findAll(pageable);


    }
}
