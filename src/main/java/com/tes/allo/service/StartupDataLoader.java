package com.tes.allo.service;

import com.tes.allo.config.FrankfurterProperties;
import com.tes.allo.fetcher.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class StartupDataLoader {

    private static final Logger log = LoggerFactory.getLogger(StartupDataLoader.class);

    private final WebClient webClient;
    private final FrankfurterProperties props;
    private final InMemoryDataStore store;

    public StartupDataLoader(WebClient webClient, FrankfurterProperties props, InMemoryDataStore store) {
        this.webClient = webClient;
        this.props = props;
        this.store = store;
    }

    @Bean
    public ApplicationRunner loadDataRunner() {
        return args -> {
            log.info("StartupDataLoader: fetching all resources from Frankfurter...");
            Map<String, Object> data = new HashMap<>();
            try {
                IDRDataFetcher latest = new LatestIdrRatesFetcher(webClient, props);
                IDRDataFetcher hist = new HistoricalIdrUsdFetcher(webClient);
                IDRDataFetcher cur = new SupportedCurrenciesFetcher(webClient);

                data.put(latest.key(), latest.fetch());
                data.put(hist.key(), hist.fetch());
                data.put(cur.key(), cur.fetch());

                store.setAll(data);
                log.info("StartupDataLoader: data loaded successfully. Keys: {}", data.keySet());
            } catch (Exception e) {
                log.error("StartupDataLoader: failed to load initial data", e);
                throw e;
            }
        };
    }
}
