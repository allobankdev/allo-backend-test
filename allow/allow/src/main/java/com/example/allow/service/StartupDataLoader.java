package com.example.allow.service;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.example.allow.strategy.IDRDataFetcher;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Component
public class StartupDataLoader implements ApplicationRunner {

    private final List<IDRDataFetcher> fetchers;
    private final DataAggregationService cache;

    public StartupDataLoader(List<IDRDataFetcher> fetchers, DataAggregationService cache) {
        this.fetchers = fetchers;
        this.cache = cache;
    }

    @Override
    public void run(ApplicationArguments args) {
        System.out.println("STARTING FRANKFURTER DATA PRELOAD...");

        Flux.fromIterable(fetchers)
                .flatMap(fetcher -> fetcher.fetchData()
                        .doOnSuccess(data -> {
                            cache.put(fetcher.getResourceKey(), data);
                            System.out.println("SUCCESS: " + fetcher.getResourceKey());
                        })
                        .doOnError(err -> {
                            System.err.println("FAILED: " + fetcher.getResourceKey() + " → " + err.getMessage());
                            cache.put(fetcher.getResourceKey(), Map.of(
                                    "error", "Failed to load: " + err.getMessage(),
                                    "resource", fetcher.getResourceKey()
                            ));
                        })
                        .onErrorResume(err -> Mono.empty())
                )
                .doFinally(signalType -> {
                    cache.markAsLoaded();
                    System.out.println("ALL DATA LOADING COMPLETED (success or partial failure)");
                })
                .blockLast(Duration.ofSeconds(30));
    }
}