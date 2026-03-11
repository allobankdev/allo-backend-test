package com.api.allorestapi.runner;

import com.api.allorestapi.model.FinanceDataResponse;
import com.api.allorestapi.store.FinanceDataStore;
import com.api.allorestapi.strategy.IDRDataFetch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoaderRunner implements ApplicationRunner {

    private final List<IDRDataFetch> fetchers;
    private final FinanceDataStore store;

    @Override
    public void run(ApplicationArguments args) {
        log.info("DataLoaderRunner starting...",
                fetchers.size());

        Map<String, FinanceDataResponse> results =
                Flux.fromIterable(fetchers)
                        .flatMap(fetcher ->
                                fetcher.fetch()
                                        .map(response -> Map.entry(fetcher.getResourceType(), response))
                                        .doOnSuccess(e -> log.info("Loaded: {}", e.getKey()))
                                        .doOnError(e -> log.error("Failed to fetch {}: {}",
                                                fetcher.getResourceType(), e.getMessage()))
                        )
                        .collectMap(Map.Entry::getKey, Map.Entry::getValue)
                        .block();

        store.load(results != null ? results : new HashMap<>());
        log.info("DataLoaderRunner complete — store is now immutable and ready");
    }
}
