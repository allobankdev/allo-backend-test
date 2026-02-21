package com.allobank.finance.config;

import com.allobank.finance.client.FrankfurterClientFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {

    @Bean
    public FrankfurterClientFactoryBean frankfurterClientFactoryBean(FinanceProperties financeProperties) {
        return new FrankfurterClientFactoryBean(financeProperties);
    }
}
