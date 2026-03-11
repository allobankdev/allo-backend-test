package com.allo.idraggregator.startup;

import java.util.Map;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.allo.idraggregator.application.service.FinanceDataService;
import com.allo.idraggregator.domain.strategy.IDRDataFetcher;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ApplicationStartup implements ApplicationRunner {

    private final Map<String, IDRDataFetcher<?>> fetchers;
    private final FinanceDataService service;

    @Override
    public void run(ApplicationArguments args) {

        fetchers.forEach((type, strategy) -> {
            
            Object data = strategy.fetchData();
            service.store(type, data);
        });
    }
}