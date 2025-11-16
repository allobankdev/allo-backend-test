package id.co.microservice.currency.currency_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiClientConfig {

    @Bean
    public RestTemplateFactoryBean restTemplateFactoryBean(ExternalApiConfig externalApiConfig) {
        return new RestTemplateFactoryBean(externalApiConfig);
    }

}
