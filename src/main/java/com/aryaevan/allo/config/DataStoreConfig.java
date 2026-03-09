package com.aryaevan.allo.config;

import com.aryaevan.allo.store.FinanceDataStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for the in-memory data store.
 */
@Configuration
public class DataStoreConfig {
    
    /**
     * Creates a singleton FinanceDataStore bean.
     * 
     * @return FinanceDataStore instance
     */
    @Bean
    public FinanceDataStore financeDataStore() {
        return new FinanceDataStore();
    }
}
