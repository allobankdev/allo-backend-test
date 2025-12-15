package com.allobank.frankfurter_aggregator.runner;

import java.util.Map;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.allobank.frankfurter_aggregator.service.DataStorageService;
import com.allobank.frankfurter_aggregator.service.strategy.DataFetcherStrategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoaderRunner implements ApplicationRunner {
    
    private final Map<String, DataFetcherStrategy> strategies;
    private final DataStorageService dataStorageService;
    
    @Override
    public void run(ApplicationArguments args) {
        log.info("Starting data loading process...");
        
        Flux.fromIterable(strategies.values())
                .flatMap(strategy -> strategy.fetchData()
                        .doOnSuccess(data -> {
                            if (data != null) {
                                dataStorageService.storeData(strategy.getResourceType(), data);
                                log.info("Successfully loaded data for: {}", strategy.getResourceType());
                            }
                        })
                        .onErrorResume(e -> {
                            log.error("Failed to load data for {}: {}", 
                                    strategy.getResourceType(), e.getMessage());
                            return Mono.empty();
                        }))
                .collectList()
                .doOnTerminate(() -> {
                    dataStorageService.setLoaded(true);
                    log.info("Data loading completed. Total resources loaded: {}", 
                            dataStorageService.getAllData().size());
                })
                .subscribe();
    }
}