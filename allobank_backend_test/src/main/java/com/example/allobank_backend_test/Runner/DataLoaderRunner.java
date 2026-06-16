package com.example.allobank_backend_test.Runner;

import com.example.allobank_backend_test.Service.DataStoreService;
import com.example.allobank_backend_test.Service.IDRDataFetcher;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DataLoaderRunner implements ApplicationRunner {
    private final List<IDRDataFetcher> fetchers;
    private final DataStoreService dataStoreService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<String, Object> result = new HashMap<>();

        for (IDRDataFetcher fetcher : fetchers) {
            result.put(fetcher.getType(), fetcher.fetch());
        }

        dataStoreService.saveAll(result);
    }
}
