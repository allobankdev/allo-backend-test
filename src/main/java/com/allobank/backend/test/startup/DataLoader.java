package com.allobank.backend.test.startup;

import com.allobank.backend.test.model.DataStore;
import com.allobank.backend.test.service.FrankfurterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {

    private final FrankfurterService service;
    private final DataStore store;

    @Override
    public void run(ApplicationArguments args) {
        store.setLatestRates(service.getLatestRates());
        store.setCurrencies(service.getCurrencies());
        store.setHistoricalRates(service.getHistoricalRates());
    }
}