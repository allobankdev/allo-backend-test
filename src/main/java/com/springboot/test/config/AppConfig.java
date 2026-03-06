package com.springboot.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() throws Exception {
        RestTemplateFactoryBean factoryBean = new RestTemplateFactoryBean();
        return factoryBean.getObject();
    }
}
