package com.allo.backend;

import com.allo.backend.service.FinanceDataStoreService;
import com.allo.backend.strategy.IDRDataFetcher;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoaderRunner implements ApplicationRunner {
    private final FinanceDataStoreService dataStoreService;
    private final List<IDRDataFetcher> fetchers;

    public DataLoaderRunner(FinanceDataStoreService dataStoreService, List<IDRDataFetcher> fetchers) {
        this.dataStoreService = dataStoreService;
        this.fetchers = fetchers;
    }

    @Override
    public void run(ApplicationArguments args) {
        dataStoreService.loadData(fetchers);
    }
}
