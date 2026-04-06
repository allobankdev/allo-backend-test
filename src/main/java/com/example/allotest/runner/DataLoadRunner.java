package com.example.allotest.runner;

import java.util.concurrent.CompletableFuture;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.example.allotest.client.FrankfurterClient;
import com.example.allotest.service.DataStoreService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DataLoadRunner implements ApplicationRunner {

    private final FrankfurterClient client;
    private final DataStoreService store;

    public DataLoadRunner(FrankfurterClient client, DataStoreService store) {
        this.client = client;
        this.store = store;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        CompletableFuture<Void> latest = CompletableFuture.runAsync(() -> store.save("latest_idr_rates", client.getLatest()));
        CompletableFuture<Void> historical = CompletableFuture.runAsync(() -> store.save("historical_idr_usd", client.getHistorical()));
        CompletableFuture<Void> currencies = CompletableFuture.runAsync(() -> store.save("supported_currencies", client.getSupportedCurrencies()));
        CompletableFuture.allOf(latest, historical, currencies).join();
        log.info("Data loading completed");
    }
    
}
