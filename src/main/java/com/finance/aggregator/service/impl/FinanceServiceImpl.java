package com.finance.aggregator.service.impl;

import com.finance.aggregator.dto.UnifiedResponseDTO;
import com.finance.aggregator.service.DataStoreService;
import com.finance.aggregator.service.FinanceService;
import com.finance.aggregator.strategy.DataFetcherStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FinanceServiceImpl implements FinanceService {

    private final DataStoreService dataStoreService;
    private final Map<String, DataFetcherStrategy> strategyMap;
    private static final Set<String> VALID_TYPES = Set.of(
            "latest_idr_rates",
            "historical_idr_usd",
            "supported_currencies"
    );

    public FinanceServiceImpl(DataStoreService dataStoreService, List<DataFetcherStrategy> strategies) {
        this.dataStoreService = dataStoreService;
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        DataFetcherStrategy::getType,
                        strategy -> strategy
                ));
        log.info("Registered strategies: {}", strategyMap.keySet());
    }

    @Override
    public Object getData(String resourceType) {
        log.info("Request for resource type: {}", resourceType);

        if (!VALID_TYPES.contains(resourceType)) {
            throw new IllegalArgumentException("Invalid resource type: " + resourceType +
                    ". Valid types: " + VALID_TYPES);
        }

        Object data = dataStoreService.ambilData(resourceType);

        if (data == null) {
            throw new IllegalStateException("Data not available for: " + resourceType +
                    ". Please wait for application startup to complete.");
        }

        return UnifiedResponseDTO.builder()
                .resourceType(resourceType)
                .data(data)
                .timestamp(System.currentTimeMillis())
                .status("SUCCESS")
                .build();
    }
}