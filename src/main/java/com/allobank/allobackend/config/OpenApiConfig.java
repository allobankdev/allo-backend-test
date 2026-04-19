package com.allobank.allobackend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenApi(){
        return  new OpenAPI().info(new Info().
                title("Exchange Rate API").
                version("1.0").
                description("This api for Exchange Rate API").
                contact(new Contact().name("Ashri Prastiko Juned").email("ashriprastiko78@gmail.com").
                        url("https://github.com/ashrijavadev")));
    }

}
