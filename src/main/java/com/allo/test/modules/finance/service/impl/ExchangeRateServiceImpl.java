package com.allo.test.modules.finance.service.impl;

import com.allo.test.modules.finance.client.IDRDataFetcher;
import com.allo.test.modules.finance.enums.ResourceType;
import com.allo.test.modules.finance.exceptions.InvalidResourceTypeException;
import com.allo.test.modules.finance.service.DataStoreService;
import com.allo.test.modules.finance.service.ExchangeRateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Implementation of ExchangeRateService for accessing IDR exchange rate data.
 * <p>
 * Delegates all data retrieval to strategy implementations.
 * Each strategy is self-contained and handles its own data lifecycle.
 * <p>
 * All fields are final to ensure immutability after initialization.
 */
@Slf4j
@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final Map<ResourceType, IDRDataFetcher> strategies;
    private final DataStoreService dataStoreService;

    /**
     * Constructs the service and builds the immutable strategy map.
     *
     * @param strategies list of all IDRDataFetcher implementations (auto-injected by Spring)
     * @param dataStoreService  the data store for checking data availability
     */
    public ExchangeRateServiceImpl(List<IDRDataFetcher> strategies, DataStoreService dataStoreService) {
        this.dataStoreService = dataStoreService;
        this.strategies = strategies.stream()
                .collect(Collectors.toUnmodifiableMap(
                        IDRDataFetcher::getResourceType,
                        Function.identity()
                ));
        log.info("Initialized ExchangeRateService with {} strategies", this.strategies.size());
    }

    @Override
    public Object getData(String resourceType) {
        log.debug("Retrieving data for resource type: {}", resourceType);

        ResourceType type = ResourceType.fromString(resourceType);
        if (type == null) {
            log.warn("Invalid resource type requested: {}", resourceType);
            throw new InvalidResourceTypeException();
        }

        return strategies.get(type).getData();
    }

    @Override
    public boolean isDataAvailable() {
        return dataStoreService.isDataLoaded();
    }
}
