package com.api.allorestapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Frankfurter IDR Exchange Rate API")
                        .description("""
                                Aggregates Indonesian Rupiah (IDR) exchange-rate data from the \
                                public Frankfurter API into a single polymorphic endpoint.
                                
                                **GitHub:** MRafi68 | **Spread Factor:** 0.00637
                                
                                **USD_BuySpread_IDR Formula:**
                                `(1 / Rate_USD) * (1 + 0.00637)`
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("MRafi68")
                                .url("https://github.com/MRafi68")))
                .servers(List.of(
                        new Server().url("http://localhost:8081").description("Local Development")
                ));
    }
}