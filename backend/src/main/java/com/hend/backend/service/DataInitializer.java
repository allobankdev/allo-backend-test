package com.hend.backend.service;

import com.hend.backend.strategy.IDRDataFetcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author : hend wunga
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements ApplicationRunner {

    private final List<IDRDataFetcher> strategies;
    private final FinanceDataStorage storage;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Starting initial data ingestion from Frankfurter API...");

        for (IDRDataFetcher strategy : strategies) {
            try {
                Object data = strategy.fetchData();
                storage.saveData(strategy.getResourceType(), data);
                log.info("Successfully loaded resource: {}", strategy.getResourceType());
            } catch (Exception e) {
                log.error("Failed to load resource: {}. Error: {}", strategy.getResourceType(), e.getMessage());
            }
        }

        storage.lockStorage();
        log.info("In-memory storage is now locked and immutable.");
    }
}
