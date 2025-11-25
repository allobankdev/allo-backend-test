package com.example.financedata.runner;

import com.example.financedata.fetcher.IDRDataFetcher;
import com.example.financedata.service.FetcherRegistryService;
import com.example.financedata.store.ImmutableFinanceStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

@Component
public class StartupDataLoader implements ApplicationRunner {

    private final Logger log = LoggerFactory.getLogger(StartupDataLoader.class);
    private final FetcherRegistryService registry;
    private final ImmutableFinanceStore store;

    public StartupDataLoader(FetcherRegistryService registry, ImmutableFinanceStore store) {
        this.registry = registry;
        this.store = store;
    }

    @Override
    public void run(org.springframework.boot.ApplicationArguments args) {
        log.info("StartupDataLoader: fetching finance data for all registered fetchers.");
        Map<String, IDRDataFetcher> all = registry.all();

        for (IDRDataFetcher fetcher : all.values()) {
            try {
                Object result = fetcher.fetch()
                                .timeout(Duration.ofSeconds(60)) // shorter timeout
                                .onErrorResume(e -> {
                                    log.error("Error fetching resource {}: {}", fetcher.resourceKey(), e.getMessage(), e);
                                    return Mono.empty(); // return empty instead of throwing
                                })
                                .block();

                if (result != null) {
                    store.put(fetcher.resourceKey(), result);
                    log.info("Loaded resource {}", fetcher.resourceKey());
                } else {
                    log.warn("Skipped loading resource {} due to previous errors", fetcher.resourceKey());
                }
            } catch (Exception ex) {
                // this should rarely trigger now
                log.error("Unexpected error loading resource {}: {}", fetcher.resourceKey(), ex.getMessage(), ex);
            }
        }

        store.markLoaded();
        log.info("StartupDataLoader: all resources loaded and store marked immutable.");
    }
}

