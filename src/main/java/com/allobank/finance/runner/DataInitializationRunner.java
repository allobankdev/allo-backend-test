package com.allobank.finance.runner;

import com.allobank.finance.cache.IDRDataFetcherCache;
import com.allobank.finance.registry.IDRDataFetcherRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializationRunner implements ApplicationRunner {

    private final IDRDataFetcherRegistry idrDataFetcherRegistry;

    private final IDRDataFetcherCache idrDataFetcherCache;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Starting data initialization...");

        idrDataFetcherRegistry.getAll()
                .forEach((resourceType, dataFetcher) ->
                        idrDataFetcherCache.put(resourceType, dataFetcher.fetchData()));

        idrDataFetcherCache.markInitialized();

        log.info("Data initialization completed successfully");
    }
}
