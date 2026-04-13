package com.allobank.test.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Allo Backend Test API",
                version = "v1",
                description = "API documentation for finance data endpoints.",
                contact = @Contact(name = "Allo Backend Candidate")
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local server")
        }
)
public class OpenApiConfig {
}
