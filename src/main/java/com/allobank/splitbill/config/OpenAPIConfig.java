package com.allobank.splitbill.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI splitBillOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Allo Bank Split Bill REST API")
                        .description("Production-grade REST API for shared expense management, debt simplification, and settlement calculation.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Allo Bank Take-Home Challenge Candidate")
                                .url("https://github.com/resa-rm/allo-backend-test")));
    }
}
