package com.allobank.services;

import com.allobank.strategy.IDRDataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class DataPreloader implements ApplicationRunner {

    private final Map<String, IDRDataFetcher> fetchers;
    private final DataCacheService cache;

    public DataPreloader(Map<String, IDRDataFetcher> fetchers, 
                        DataCacheService cache) {
        this.fetchers = fetchers;
        this.cache = cache;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("Memulai preload data...");
        
        try {
            for (IDRDataFetcher strategy : fetchers.values()) {
                String key = strategy.getResourceType();
                log.info("Loading: {}", key);
                
                Object data = strategy.fetchData();
                cache.put(key, data);
                
                log.info("✓ {} loaded", key);
            }
            cache.markReady();
            log.info("Semua data siap!");
            
        } catch (Exception e) {
            log.error("Gagal preload data: {}", e.getMessage());
        }
    }
}
