package com.athallah.finance.service;

import com.athallah.finance.startegy.IDRDataFetcher;
import com.athallah.finance.util.constant.ResourceType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class FinanceService {

    private final Map<String, IDRDataFetcher> strategyMap;

    public Object getData(ResourceType resourceType) {
        String key = resourceType.name();

        IDRDataFetcher strategy = strategyMap.get(key);

        if (strategy == null) {
            throw new IllegalArgumentException(
                    "Unknown resource type: " + resourceType
            );
        }

        return strategy.fetchData();
    }
}
