package com.example.allobank.project.service;

import java.util.Map;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.example.allobank.project.strategy.IDRDataFetcher;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataLoaderRunner implements ApplicationRunner {

    private final DataMemoryStore dataStore;
    private final Map<String, IDRDataFetcher> fetchers;

    @Override
    public void run(ApplicationArguments args) {
        fetchers.forEach((resourceKey, fetcher) -> {
            Object data = fetcher.fetchData();
            dataStore.put(resourceKey, data);
        });
    }
}