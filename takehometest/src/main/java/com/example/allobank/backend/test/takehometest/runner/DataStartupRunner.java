package com.example.allobank.backend.test.takehometest.runner;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.example.allobank.backend.test.takehometest.fetcher.DataFetcher;
import com.example.allobank.backend.test.takehometest.store.DataStore;

@Component
public class DataStartupRunner implements ApplicationRunner {

    private final DataStore store;
    private final Map<String, DataFetcher> fetcherMap;

    public DataStartupRunner(
            List<DataFetcher> fetchers,
            DataStore store) {

        this.store = store;
        this.fetcherMap = fetchers.stream()
                .collect(Collectors.toMap(
                        DataFetcher::getResourceType,
                        Function.identity()));
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        fetcherMap.forEach((key, fetcher) -> store.put(key, fetcher.fetchData()));
    }
}
