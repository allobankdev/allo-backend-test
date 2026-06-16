package com.allobank.config;

import com.allobank.factory.WebClientFactoryBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration class that registers WebClient bean using FactoryBean.
 * This ensures the WebClient is properly created from the FactoryBean.
 */
@Configuration
public class WebClientConfig {

    @Bean
    public FactoryBean<WebClient> webClientFactoryBean(FrankfurterApiProperties apiProperties) {
        return new WebClientFactoryBean(apiProperties);
    }

    @Bean
    public WebClient webClient(FactoryBean<WebClient> webClientFactoryBean) throws Exception {
        return webClientFactoryBean.getObject();
    }
}

