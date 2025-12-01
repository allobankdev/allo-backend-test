package com.app.others;

import com.app.strategy.IDRDataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class ExchangeAggregatorRunner implements ApplicationRunner {

    private final Map<String, IDRDataFetcher<?>> fetcherMap;

    public ExchangeAggregatorRunner(Map<String, IDRDataFetcher<?>> fetcherMap) {
        this.fetcherMap = fetcherMap;
    }

    @Override
    public void run(ApplicationArguments args){
        log.info("Processing all resources...");

        for (Map.Entry<String, IDRDataFetcher<?>> e : fetcherMap.entrySet()) {

            IDRDataFetcher<?> fetcher = e.getValue();

            fetcher.execute();

        }

        log.info("The fetching steps have been completed");

    }
}
