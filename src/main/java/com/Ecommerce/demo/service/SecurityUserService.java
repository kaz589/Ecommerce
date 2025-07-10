package com.Ecommerce.demo.service;

import com.Ecommerce.demo.model.entity.UsersEntity;
import com.Ecommerce.demo.model.po.SecurityUser;
import com.Ecommerce.demo.repository.UsersRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SecurityUserService implements UserDetailsService {
    private final UsersRepository usersRepository;

    public SecurityUserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        // 先試著當 username 查
        Optional<UsersEntity> users = usersRepository.findByUsername(username);
        //再用Email查
        if (users.isEmpty()) {
            //throw new UsernameNotFoundException("使用者不存在: " + username);
            users = usersRepository.findByEmail(username);
        }


        // 如果還是找不到，拋出 UsernameNotFoundException
        if (users.isEmpty()) {
            throw new UsernameNotFoundException("使用者不存在: " + username);
        }

        // 如果找到，則返回 MyUser 物件
        return new SecurityUser(users.get());
    }
}
