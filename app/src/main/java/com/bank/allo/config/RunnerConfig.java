package com.bank.allo.config;

import com.bank.allo.repository.inbound.DataStore;
import com.bank.allo.runner.DataLoadRunner;
import com.bank.allo.usecase.idr.FetchIdrDataUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RunnerConfig {

    @Bean
    public DataLoadRunner dataLoadRunner(
            FetchIdrDataUseCase fetchUseCase,
            DataStore dataStore
    ) {
        return new DataLoadRunner(fetchUseCase, dataStore);
    }
}
