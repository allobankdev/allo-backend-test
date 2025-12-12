package com.allobank.finance.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import com.allobank.finance.client.FrankfurterClient;
import com.allobank.finance.client.FrankfurterWebClientFactory;

@Configuration
@EnableConfigurationProperties(FrankfurterClientProperties.class)
public class FrankfurterClientConfiguration {

    @Bean
    public WebClient frankfurterWebClient(FrankfurterClientProperties props) {
        FrankfurterWebClientFactory factory = new FrankfurterWebClientFactory(props);
        return factory.getObject();
    }

    @Bean
    public FrankfurterClient frankfurterClient(WebClient frankfurterWebClient) {
        return new FrankfurterClient(frankfurterWebClient);
    }
}
