package com.frankfurter.aggregator.runner;

import com.frankfurter.aggregator.service.DataFetchingService;
import com.frankfurter.aggregator.service.DataStorageService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupDataRunner implements ApplicationRunner {
    private final DataFetchingService dataFetchingService;
    private final DataStorageService dataStorageService;

    public StartupDataRunner(DataFetchingService dataFetchingService, 
                           DataStorageService dataStorageService) {
        this.dataFetchingService = dataFetchingService;
        this.dataStorageService = dataStorageService;
    }

    @Override
    public void run(ApplicationArguments args) {
        System.out.println("Fetching data from Frankfurter API on startup...");
        try {
            dataFetchingService.fetchAndStoreAllData(dataStorageService);  // No .block()
            System.out.println("Successfully loaded " + dataStorageService.getAllData().size() + " resources");
        } catch (Exception e) {
            System.err.println("Failed to load data: " + e.getMessage());
        }
    }
}
