package com.chikohakles.allobank.agregator.bs;

import com.chikohakles.allobank.agregator.constant.ResourceType;
import com.chikohakles.allobank.agregator.store.AgregatorDataStore;
import com.chikohakles.allobank.agregator.strategy.BaseStrategy;
import com.chikohakles.allobank.agregator.strategy.BaseStrategyFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AgregatorDataPreloader implements ApplicationRunner {

    private final BaseStrategyFactory baseStrategyFactory;
    private final AgregatorDataStore agregatorDataStore;

    @Override
    public void run(ApplicationArguments args) {
        log.info("AgregatorDataPreloader start loading data");

        for(ResourceType resource : ResourceType.values()) {
            BaseStrategy strategy = baseStrategyFactory.getStrategy(resource);

            try {
                Object data = strategy.getData();
                agregatorDataStore.put(resource, data);
                log.info("AgregatorDataPreloader successfully load data for type {}", resource);
            } catch (Exception e) {
                log.warn("AgregatorDataPreloader fail to fetch data for type {}", resource.getCode(), e);
            }
        }

        log.info("AgregatorDataPreloader finish loading data");
    }
}
