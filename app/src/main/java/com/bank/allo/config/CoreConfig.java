package com.bank.allo.config;

import com.bank.allo.usecase.idr.FetchIdrDataUseCase;
import com.bank.allo.usecase.idr.IdrDataFetcher;
import java.util.Map;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoreConfig {

    @Bean
    public FetchIdrDataUseCase fetchIdrDataUseCase(
            @Qualifier("idrFetcherRegistry")
            Map<String, IdrDataFetcher> fetcherRegistry
    ) {
        return new FetchIdrDataUseCase(fetcherRegistry);
    }
}
