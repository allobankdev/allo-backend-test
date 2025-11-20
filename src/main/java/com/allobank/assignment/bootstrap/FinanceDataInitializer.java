package com.allobank.assignment.bootstrap;

import com.allobank.assignment.exception.ExternalServiceException;
import com.allobank.assignment.model.FinanceDataResponse;
import com.allobank.assignment.model.ResourceType;
import com.allobank.assignment.service.FinanceDataCache;
import com.allobank.assignment.strategy.IdrDataFetchStrategy;
import com.allobank.assignment.strategy.IdrDataStrategyRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class FinanceDataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(FinanceDataInitializer.class);

    private final IdrDataStrategyRegistry strategyRegistry;
    private final FinanceDataCache cache;

    public FinanceDataInitializer(IdrDataStrategyRegistry strategyRegistry, FinanceDataCache cache) {
        this.strategyRegistry = strategyRegistry;
        this.cache = cache;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<ResourceType, FinanceDataResponse> responses = new EnumMap<>(ResourceType.class);
        for (IdrDataFetchStrategy strategy : strategyRegistry.getAllStrategies()) {
            ResourceType resourceType = strategy.supports();
            try {
                log.info("Preloading data for resource {}", resourceType.value());
                FinanceDataResponse response = strategy.fetch();
                responses.put(resourceType, response);
            } catch (ExternalServiceException ex) {
                log.error("Failed to preload data for {}: {}", resourceType.value(), ex.getMessage());
                throw ex;
            }
        }
        cache.initialize(responses);
        log.info("Finance data cache initialized with {} resources", responses.size());
    }
}
