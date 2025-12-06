package com.bank.allo.config;

import com.bank.allo.client.FrankfurterClientRepositoryImpl;
import com.bank.allo.client.FrankfurterWebClientFactoryBean;
import com.bank.allo.properties.FrankfurterProperties;
import com.bank.allo.repository.inbound.DataStore;
import com.bank.allo.repository.outbound.FrankfurterClientRepository;
import com.bank.allo.store.InMemoryDataStoreImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class DataConfig {

    @Bean
    public FrankfurterWebClientFactoryBean frankfurterWebClientFactoryBean(
            FrankfurterProperties props
    ) {
        return new FrankfurterWebClientFactoryBean(props.getBaseUrl());
    }

    @Bean
    public FrankfurterClientRepository frankfurterClientRepository(WebClient webClient) {
        return new FrankfurterClientRepositoryImpl(webClient);
    }

    @Bean
    public DataStore dataStore() {
        return new InMemoryDataStoreImpl();
    }
}
