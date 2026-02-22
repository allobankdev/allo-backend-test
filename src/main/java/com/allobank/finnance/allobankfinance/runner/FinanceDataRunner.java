package com.allobank.finnance.allobankfinance.runner;

import com.allobank.finnance.allobankfinance.integration.FrankfurterIntegrationService;
import com.allobank.finnance.allobankfinance.service.storage.FinanceStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class FinanceDataRunner {

    private final FinanceStorage storage;
    private final FrankfurterIntegrationService integrationService;

    @Bean
    public ApplicationRunner loadDataAtStartup() {
        return args -> {
            Map<String, Object> aggregatedData = Map.of(
                    "latest", integrationService.getLatestUsdRates("IDR"),
                    "currencies", integrationService.getSupportedCurrencies(),
                    "historical", integrationService.getHistoricalRates("2025-01-19","2026-01-19","IDR","USD")
            );

            storage.setData(aggregatedData);
        };
    }
}
