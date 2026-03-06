package com.finance.aggregator.service;

import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.finance.aggregator.strategy.IDRDataFetcher;

@Component
public class AppStartupRunner implements ApplicationRunner {
    private final List<IDRDataFetcher> strategies;
    private final FinanceDataService service;
    private final WebClient client;

    public AppStartupRunner(List<IDRDataFetcher> strategies, FinanceDataService service, WebClient client) {
        this.strategies = strategies;
        this.service = service;
        this.client = client;
    }

    @Override
    public void run(ApplicationArguments args) {
        service.refreshCache(strategies, client);
        System.out.println("✅ In-Memory Data Loaded Successfully!");
    }
}