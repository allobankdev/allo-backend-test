package com.sdewa.IdrRateAggregator.bootstrap;

import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.sdewa.IdrRateAggregator.services.AppDataStore;
import com.sdewa.IdrRateAggregator.services.IDRDataFetcher;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StartupDataLoader implements ApplicationRunner {
    private final List<IDRDataFetcher<?>> fetchers;
    private final AppDataStore dataStore;

    @Override
    public void run(ApplicationArguments args) {
        fetchers.forEach(f -> dataStore.put(f.getResourceType(), f.fetchData()));
    }
}
