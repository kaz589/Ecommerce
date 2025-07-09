package com.Ecommerce.demo.model.po;

import com.Ecommerce.demo.model.entity.UsersEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SecurityUser implements UserDetails {
    private final UsersEntity users;

    public SecurityUser(UsersEntity users) {
        this.users = users;
    }

    @Override
    public List<SimpleGrantedAuthority> getAuthorities() {
        // 獲取 authority 並檢查是否為 null
        String authority = users.getRole().getRoleName();

        // 如果 authority 為 null，則返回一個空的列表
        if (authority == null) {
            return Collections.emptyList();  // 或者返回某個默認權限
        }

        // 如果 authority 不為 null，則進行分割並轉換為權限列表
        List<SimpleGrantedAuthority> authorities = Arrays.stream(authority.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return authorities;
    }

    @Override
    public String getPassword() {
        return users.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return users.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
