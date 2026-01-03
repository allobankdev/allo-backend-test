package com.allobank.allobackendtest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.allobank.allobackendtest.client.FrankfurterWebClientFactoryBean;

@Configuration
public class FrankfurterClientConfig {

    @Bean
    public FrankfurterWebClientFactoryBean frankfurterWebClientFactoryBean(FrankfurterProperties properties){
        return new FrankfurterWebClientFactoryBean(properties);
    }

}
