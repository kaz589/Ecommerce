package com.Ecommerce.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hellowspring {

    @GetMapping("/hello")
    public String helloWorld() {
        return "Hello, World!";
    }
}

