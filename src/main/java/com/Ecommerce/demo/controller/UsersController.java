package com.Ecommerce.demo.controller;

import com.Ecommerce.demo.model.entity.UsersEntity;
import com.Ecommerce.demo.service.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")

public class UsersController {
    private UsersService usersService;


    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @Operation(
            summary = "搜尋所有用戶",
            description = "測試搜尋所有用戶"
    )
    @GetMapping("/All")
    public Page<UsersEntity> findAllUsers(){
        Page<UsersEntity> aas = usersService.getAllUsers(0,10);

        System.out.println(usersService.getAllUsers(0, 10).getContent());
        return  usersService.getAllUsers(0,10);
    }
}
