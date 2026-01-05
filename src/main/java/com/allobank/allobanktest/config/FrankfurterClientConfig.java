package com.allobank.allobanktest.config;

import com.allobank.allobanktest.client.FrankfurterClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FrankfurterClientConfig {

    @Bean
    public FrankfurterClientFactory frankfurterClientFactory(
            FrankfurterProperties properties
    ) {
        return new FrankfurterClientFactory(properties);
    }

}
