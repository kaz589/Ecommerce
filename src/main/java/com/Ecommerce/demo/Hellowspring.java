package com.Ecommerce.demo;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hellowspring {

    @Operation(
            summary = "Hello, World測試",
            description = "測試Hello, World"
    )
    @GetMapping("/hello")
    public String helloWorld() {
        return "Hello, World!";
    }
}

