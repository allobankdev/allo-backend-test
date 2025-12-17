package com.example.allobank.runner;

import com.example.allobank.dto.FinanceDataItemDto;
import com.example.allobank.service.IDRDataFetcher;
import com.example.allobank.storage.DataStorageService;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoaderRunner implements ApplicationRunner {

    private final List<IDRDataFetcher> fetchers;
    private final DataStorageService storage;

    @Override
    public void run(ApplicationArguments args) {
        Map<String, List<FinanceDataItemDto>> loaded = new LinkedHashMap<>();

        for (IDRDataFetcher fetcher : fetchers) {
            String type = fetcher.resourceType();
            List<FinanceDataItemDto> data = fetcher.fetch();
            loaded.put(type, data);
        }

        storage.initialize(loaded);
    }
}