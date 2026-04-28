package org.imam.allo.runner;

import lombok.extern.slf4j.Slf4j;
import org.imam.allo.service.DataStoreService;
import org.imam.allo.service.strategy.IDRDataFetcher;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class DataLoaderRunner implements ApplicationRunner {
    private final List<IDRDataFetcher> fetchers;
    private final DataStoreService store;

    public DataLoaderRunner(List<IDRDataFetcher> fetchers,
                            DataStoreService store) {
        this.fetchers = fetchers;
        this.store = store;
    }

    @Override
    public void run(ApplicationArguments args) {
        for (IDRDataFetcher fetcher : fetchers) {
            Object data = fetcher.fetchData();

            log.info("Loaded resource: {}", fetcher.getResourceType());
            log.debug("Data: {}", data);

            store.put(fetcher.getResourceType(), data);
        }
    }
}
