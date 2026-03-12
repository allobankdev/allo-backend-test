package com.allobank.financeapi.runner;

import com.allobank.financeapi.model.DataStore;
import com.allobank.financeapi.model.FinanceData;
import com.allobank.financeapi.model.LatestIdrWithSpread;
import com.allobank.financeapi.service.strategy.StrategyRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializationRunner implements ApplicationRunner {

    private final StrategyRegistry strategyRegistry;
    private final DataStore dataStore;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Starting data initialization from Frankfurter API...");

        CountDownLatch latch = new CountDownLatch(1);

        Flux.zip(
                fetchLatestRates(),
                fetchHistoricalData(),
                fetchSupportedCurrencies()
        ).subscribe(
                tuple -> {
                    dataStore.setAllData(
                            (List<LatestIdrWithSpread>) tuple.getT1().getData(),
                            (Map<String, Object>) tuple.getT2().getData(),
                            (Map<String, String>) tuple.getT3().getData()
                    );
                    log.info("Data initialization completed successfully");
                    latch.countDown();
                },
                error -> {
                    log.error("Failed to initialize data: {}", error.getMessage());
                    latch.countDown();
                }
        );

        if (!latch.await(30, TimeUnit.SECONDS)) {
            log.error("Data initialization timed out");
        }
    }

    private Mono<FinanceData> fetchLatestRates() {
        return strategyRegistry.getStrategy("latest_idr_rates").fetchData();
    }

    private Mono<FinanceData> fetchHistoricalData() {
        return strategyRegistry.getStrategy("historical_idr_usd").fetchData();
    }

    private Mono<FinanceData> fetchSupportedCurrencies() {
        return strategyRegistry.getStrategy("supported_currencies").fetchData();
    }
}