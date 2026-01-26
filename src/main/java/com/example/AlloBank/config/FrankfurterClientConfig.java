package com.example.AlloBank.config;


import com.example.AlloBank.client.FrankfurterRestTemplateFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FrankfurterClientConfig {

    @Bean
    public FrankfurterRestTemplateFactoryBean frankfurterRestTemplate(
            @Value("${frankfurter.base-url}") String baseUrl
    ) {
        return new FrankfurterRestTemplateFactoryBean(baseUrl);
    }

}
