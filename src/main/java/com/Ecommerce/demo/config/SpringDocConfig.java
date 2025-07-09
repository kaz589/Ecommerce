package com.Ecommerce.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition
@Configuration
public class SpringDocConfig {
    @Bean
    public OpenAPI baseOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI().info(new Info().title("API幫助文件")
                .version("v0.0.1").description("SpringBoot 3.x application"));


    }
}
