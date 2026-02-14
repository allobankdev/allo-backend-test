package com.allo.test.strategy;

import com.allo.test.constant.GenericConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FinanceStrategyFactory {

    private final Map<String, IDRDataFetcher> strategyMap;

    public FinanceStrategyFactory(List<IDRDataFetcher> fetchers) {
        this.strategyMap = fetchers.stream()
                .collect(Collectors.toMap(
                        IDRDataFetcher::getResourceType,
                        Function.identity()
                ));
    }

    public IDRDataFetcher getStrategy(String resourceType) {
        IDRDataFetcher strategy = strategyMap.get(resourceType);

        if (strategy == null) {
            throw new IllegalArgumentException(GenericConstant.INVALID_RESOURCE);
        }

        return strategy;
    }
}
