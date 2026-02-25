package com.allobank.finance.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Allo Bank Finance API")
                        .version("v1")
                        .description("""
                            IDR Exchange Rate Aggregator.
                            
                            Data is fetched from Frankfurter API once at startup and served from an immutable in-memory store.
                            
                            **Spread Factor:** 0.00264 (GitHub username: thaufaniqbal)
                            """)
                        .contact(new Contact()
                                .name("thaufaniqbal")
                                .url("https://github.com/thaufaniqbal")));
    }
}
