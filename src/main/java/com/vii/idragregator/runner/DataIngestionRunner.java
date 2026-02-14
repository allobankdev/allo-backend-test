package com.vii.idragregator.runner;

import com.vii.idragregator.service.FinanceDataService;
import com.vii.idragregator.strategy.IDRDataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Luthfi Aryarizki
 * @date Created on 2026/02/14 at 09:00 p.m
 */
@Slf4j
@Component
public class DataIngestionRunner implements ApplicationRunner {

    @Autowired
    private Map<String, IDRDataFetcher> strategyMap;

    @Autowired
    private FinanceDataService dataService;

    @Override
    public void run(ApplicationArguments args) {
        log.info("start initializing data ingestion from Frankfurter API at startup");

        strategyMap.forEach((resourceName, strategy) -> {
            try {
                log.info("Fetching data for resource: {}", resourceName);
                Object fetchedData = strategy.fetch();
                dataService.initializeData(resourceName, fetchedData);
                log.info("Successfully processed resource: {}", resourceName);
            } catch (Exception e) {
                log.error("Failed to fetch data for {}: {}", resourceName, e.getMessage());
            }
        });

        dataService.lockStorage();
        log.info("end data ingestion completed. In-memory storage is now immutable.");
    }

}
