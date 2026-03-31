package com.allo.test.service.loader;

import com.allo.test.service.strategy.IDRDataFetcher;
import com.allo.test.store.DataStore;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {

    private final List<IDRDataFetcher> fetchers;
    private final DataStore store;

    @Override
    public void run(ApplicationArguments args) {

        fetchers.forEach(fetcher -> {
            var data = fetcher.fetch();
            store.put(fetcher.getType(), data);
        });
    }
}