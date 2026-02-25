package com.allobank.finance.service;

import com.allobank.finance.config.AppProperties;
import com.allobank.finance.dto.FinanceDataResponse;
import com.allobank.finance.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FinanceDataService {

    private final FinanceDataStore financeDataStore;
    private final AppProperties appProperties;

    public FinanceDataResponse getByResourceType(String resourceType) {
        log.debug("Serving request for resource type: {}", resourceType);

        if (!appProperties.getValidResourceTypes().contains(resourceType)) {
            throw new ResourceNotFoundException(
                    "Resource type '" + resourceType + "' not found. " +
                            "Valid types are: " + String.join(", ", appProperties.getValidResourceTypes())
            );
        }

        FinanceDataResponse response = financeDataStore.get(resourceType);
        if (response == null) {
            throw new ResourceNotFoundException(
                    "Resource type '" + resourceType + "' is valid but data is not available. " +
                            "Please try again later."
            );
        }

        return response;
    }
}