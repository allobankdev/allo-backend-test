package com.test.frankfurterexchangerate.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Allo Bank Backend Test",
                version = "v1",
                description = "API for Project Vue-App"
        )
)
public class SwaggerConfig {
}
