package com.allobankdev.allo_idr_rate_aggregator.service;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.allobankdev.allo_idr_rate_aggregator.strategy.IDRDataFetcher;

import java.util.AbstractMap;
import java.util.List;
import java.util.stream.Collectors;
@Component
@RequiredArgsConstructor
public class FinanceDataLoader implements ApplicationRunner {

    private final List<IDRDataFetcher> fetchers;
    private final FinanceDataStore store;

    @Override
    public void run(ApplicationArguments args) {
        // Menggunakan Flux untuk memproses fetcher secara reaktif
        Flux.fromIterable(fetchers)
            .flatMap(f -> f.fetch()
                .map(data -> new AbstractMap.SimpleEntry<String, List<Object>>(f.resourceType(), data))
            )
            .collect(Collectors.toMap(
                AbstractMap.SimpleEntry::getKey,
                AbstractMap.SimpleEntry::getValue
            ))
            .subscribe(
                resultMap -> {
                    store.initialize(resultMap);
                    System.out.println("Finance Data Store initialized successfully!");
                },
                error -> System.err.println("Error initializing data: " + error.getMessage())
            );
    }
}

