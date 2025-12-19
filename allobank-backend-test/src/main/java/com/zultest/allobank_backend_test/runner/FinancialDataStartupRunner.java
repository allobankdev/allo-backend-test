package com.zultest.allobank_backend_test.runner;

import com.zultest.allobank_backend_test.service.IDRDataFetcherInterface;
import com.zultest.allobank_backend_test.store.InMemoryStore;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FinancialDataStartupRunner implements ApplicationRunner {
    private final List<IDRDataFetcherInterface> fetchers;
    private final InMemoryStore store;

    public FinancialDataStartupRunner(List<IDRDataFetcherInterface> fetchers, InMemoryStore store) {
        this.fetchers = fetchers;
        this.store = store;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (IDRDataFetcherInterface fetcher : fetchers) {
            Object data = fetcher.fetchData();
            store.put(fetcher.resourceType(), data);
        }
        store.markInitialized();
    }

}
