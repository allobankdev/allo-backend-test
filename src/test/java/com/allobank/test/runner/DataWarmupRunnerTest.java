package com.allobank.test.runner;

import com.allobank.test.repository.FinanceDataRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DataWarmupRunnerTest {

    @Autowired
    private FinanceDataRepository repository;

    @Test
    void testDataIsLoadedOnStartup() {
        Object latestData = repository.getData("latest_idr_rates");
        Object historicalData = repository.getData("historical_idr_usd");
        Object currenciesData = repository.getData("supported_currencies");

        Assertions.assertNotNull(latestData, "Data Latest Rate gagal dimuat ke memori");
        Assertions.assertNotNull(historicalData, "Data Historical gagal dimuat ke memori");
        Assertions.assertNotNull(currenciesData, "Data Currencies gagal dimuat ke memori");

        System.out.println("Integration Test Passed! Runner successfully loaded mock data into repository.");
    }
}
