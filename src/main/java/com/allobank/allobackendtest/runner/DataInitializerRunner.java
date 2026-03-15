package com.allobank.allobackendtest.runner;

import com.allobank.allobackendtest.service.DataStoreService;
import com.allobank.allobackendtest.strategy.IDRDataFetcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializerRunner implements ApplicationRunner {
    private final List<IDRDataFetcher> strategies;
    private final DataStoreService dataStoreService;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Starting initial data fetch...");

        List<String> resourceTypes = List.of(
                "latest_idr_rates",
                "historical_idr_usd",
                "supported_currencies"
        );

        Flux.fromIterable(resourceTypes)
                .flatMap(type -> {
                    IDRDataFetcher strategy = strategies.stream()
                            .filter(s -> s.supports(type))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("No strategy found for: " + type));

                    return strategy.fetchData()
                            .doOnNext(data -> {
                                dataStoreService.storeData(type, data);
                                log.info("Successfully fetched and stored data for: {}", type);
                            });
                })
                .doOnComplete(() -> log.info("Initial data fetch completed."))
                .doOnError(e -> log.error("Error during initial data fetch", e))
                .subscribe();
    }
}
