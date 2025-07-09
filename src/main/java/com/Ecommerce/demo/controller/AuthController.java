package com.Ecommerce.demo.controller;

import com.Ecommerce.demo.filter.JwtResponse;
import com.Ecommerce.demo.model.LoginRequest;
import com.Ecommerce.demo.utils.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // 驗證用戶
//    	這行會觸發 Spring Security 去驗證帳號密碼
//    	✅ 驗證成功 → 回傳一個 Authentication
//    	❌ 驗證失敗 → 拋出 BadCredentialsException


            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            // 登入成功，生成 JWT Token
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtTokenProvider.generateToken(authentication.getName(), authentication.getAuthorities().toString());

            //回傳
            return ResponseEntity.ok(new JwtResponse(jwt));
        }
        catch (BadCredentialsException e) {
            // 捕獲錯誤並回傳具體訊息
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }
}
