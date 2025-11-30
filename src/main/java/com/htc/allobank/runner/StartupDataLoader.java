package com.htc.allobank.runner;

import com.htc.allobank.dto.FinanceDataStore;
import com.htc.allobank.strategy.IDRDataFetcher;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;

import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class StartupDataLoader implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(StartupDataLoader.class);
    private final Map<String, IDRDataFetcher> strategies;
    private final FinanceDataHolder holder;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Beginning startup data load for {} strategies", strategies.size());
        Map<String,Object> loaded = new HashMap<>();
        strategies.forEach((key, fetcher) -> {
            try {
                Object result = fetcher.fetch().subscribeOn(Schedulers.boundedElastic()).block();
                loaded.put(key, result);
                log.info("Loaded resource: {}", key);
            } catch (Exception ex) {
                log.error("Failed to load resource {}: {}", key, ex.getMessage());
                Map<String, Object> error = Map.of("error", "failed to load", "message", ex.getMessage());
                loaded.put(key, error);
            }
        });

        FinanceDataStore store = new FinanceDataStore(loaded);
        holder.setStore(store);

        log.info("Startup data load completed");
    }
}
