package com.musfiul.idrrateaggregator.runner;

import com.musfiul.idrrateaggregator.constant.ResourceType;
import com.musfiul.idrrateaggregator.service.resolver.FetchResolver;
import com.musfiul.idrrateaggregator.service.data.FinanceDataStoreService;
import com.musfiul.idrrateaggregator.service.fetcher.IDRDataFetcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FinanceStartupRunner implements ApplicationRunner {

    private final FetchResolver resolver;
    private final FinanceDataStoreService store;

    @Override
    public void run(ApplicationArguments args) {

        log.info("FinanceStartupRunner");
        for (ResourceType type : ResourceType.values()) {
            try {
                log.info("Fetching type : {}", type);
                IDRDataFetcher fetcher = resolver.resolve(type);

                if (fetcher == null) {
                    log.warn("No fetcher found for type {}", type);
                    continue;
                }
                Object data = fetcher.fetchData();
                store.put(type, data);
                log.info("Fetched data : {}", data);
            } catch (Exception e) {
                log.error("Failed to fetch data for type {}", type, e);
            }
        }
        log.info("FinanceStartupRunner end");
    }
}
