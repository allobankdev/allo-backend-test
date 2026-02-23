package com.allobank.finance.idrrateaggregator.service;

import com.allobank.finance.idrrateaggregator.exception.ResourceNotFoundException;
import com.allobank.finance.idrrateaggregator.model.dto.FinanceResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class FinanceDataStore {

    private Map<String, List<FinanceResponse>> data = Map.of();

    public void initialize(Map<String, List<FinanceResponse>> loadedData) {
        this.data = Map.copyOf(loadedData);
    }

    public List<FinanceResponse> get(String resourceType) {
        return Optional.ofNullable(data.get(resourceType))
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Resource type not found: " + resourceType
                        )
                );
    }
}
