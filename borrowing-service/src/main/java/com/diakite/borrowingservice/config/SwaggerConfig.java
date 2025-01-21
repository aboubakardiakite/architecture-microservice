package com.diakite.borrowingservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI borrowingServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Borrowing Service API")
                        .description("API pour la gestion des emprunts")
                        .version("1.0"));
    }
} 