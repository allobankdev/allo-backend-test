package com.allo.test.modules.finance.service;

import com.allo.test.modules.finance.client.strategy.IDRDataFetcher;
import com.allo.test.modules.finance.enums.ResourceType;
import com.allo.test.modules.finance.store.DataStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Service for accessing IDR exchange rate data from the in-memory store.
 * <p>
 * Delegates all data retrieval to strategy implementations.
 * Each strategy is self-contained and handles its own data lifecycle.
 * <p>
 * All fields are final to ensure immutability after initialization.
 */
@Slf4j
@Service
public class ExchangeRateService {

    private final Map<ResourceType, IDRDataFetcher> strategies;
    private final DataStore dataStore;

    /**
     * Constructs the service and builds the immutable strategy map.
     *
     * @param strategies list of all IDRDataFetcher implementations (auto-injected by Spring)
     * @param dataStore  the data store for checking data availability
     */
    public ExchangeRateService(List<IDRDataFetcher> strategies, DataStore dataStore) {
        this.dataStore = dataStore;
        this.strategies = strategies.stream()
                .collect(Collectors.toUnmodifiableMap(
                        IDRDataFetcher::getResourceType,
                        Function.identity()
                ));
        log.info("Initialized ExchangeRateService with {} strategies", this.strategies.size());
    }

    /**
     * Unified method to retrieve exchange rate data by resource type.
     * <p>
     * Valid resource types:
     * <ul>
     *   <li>latest_idr_rates - Latest IDR exchange rates with USD buy spread</li>
     *   <li>historical_idr_usd - Historical IDR to USD rates for a date range</li>
     *   <li>supported_currencies - List of all supported currency symbols</li>
     * </ul>
     *
     * @param resourceType the resource type identifier
     * @return Optional containing the requested data, or empty if not found or unavailable
     */
    public Optional<Object> getData(String resourceType) {
        log.debug("Retrieving data for resource type: {}", resourceType);

    ResourceType type = ResourceType.fromString(resourceType);
        if (type == null) {
            log.warn("Unknown resource type: {}", resourceType);
            return Optional.empty();
        }

        IDRDataFetcher strategy = strategies.get(type);
        if (strategy == null) {
            log.warn("No strategy found for resource type: {}", type);
            return Optional.empty();
        }

        return Optional.ofNullable(strategy.getData());
    }

    /**
     * Checks if all exchange rate data resources are loaded and available.
     *
     * @return true if all data is loaded, false otherwise
     */
    public boolean isDataAvailable() {
        return dataStore.isDataLoaded();
    }

}
