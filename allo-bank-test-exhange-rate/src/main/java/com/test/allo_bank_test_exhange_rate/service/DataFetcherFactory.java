package com.test.allo_bank_test_exhange_rate.service;

import java.util.Map;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.test.allo_bank_test_exhange_rate.enums.ResourceType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataFetcherFactory {
    private final Map<String, IDRDataFetcher> dataFetchers;

    public IDRDataFetcher get(String resourceType) {
        if(Stream.of(ResourceType.values()).noneMatch(r -> r.toString().equals(resourceType))) {
            throw new IllegalArgumentException("Unsupported resource type: " + resourceType);
        }
        IDRDataFetcher idrDataFetcher = dataFetchers.get(resourceType);
        return idrDataFetcher;
    }
}
