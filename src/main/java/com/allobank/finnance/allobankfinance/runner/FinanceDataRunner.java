package com.allobank.finnance.allobankfinance.runner;

import com.allobank.finnance.allobankfinance.integration.FrankfurterIntegrationService;
import com.allobank.finnance.allobankfinance.service.storage.FinanceStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class FinanceDataRunner  implements ApplicationRunner {

    private final FinanceStorage storage;
    private final FrankfurterIntegrationService integrationService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        log.info("Preloading finance data at startup...");

        Map<String, Object> aggregatedData = Map.of(
                "latest_idr_rates", integrationService.getLatestUsdRates("IDR"),
                "supported_currencies", integrationService.getSupportedCurrencies(),
                "historical_idr_usd", integrationService.getHistoricalRates(
                        "2024-01-01",
                        "2024-01-05",
                        "IDR",
                        "USD"
                )
        );

        storage.initialize(aggregatedData);

        log.info("Finance data loaded successfully.");
    }
}
