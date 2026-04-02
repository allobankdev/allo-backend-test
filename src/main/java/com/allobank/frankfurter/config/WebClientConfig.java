package com.allobank.frankfurter.config;

import com.allobank.frankfurter.client.WebClientFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.Duration;

@Configuration
public class WebClientConfig {

    private final FrankfurterApiProperties apiProperties;

    public WebClientConfig(FrankfurterApiProperties apiProperties) {
        this.apiProperties = apiProperties;
    }

    @Bean
    public WebClientFactoryBean webClientFactoryBean() {
        return new WebClientFactoryBean(
                apiProperties.getBaseUrl(),
                Duration.ofMillis(apiProperties.getConnectTimeout()),
                Duration.ofMillis(apiProperties.getReadTimeout())
        );
    }
}