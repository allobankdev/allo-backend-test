package com.allobank.exercise.api.service.impl;

import com.allobank.exercise.api.enumeration.ResourceType;
import com.allobank.exercise.api.service.IDRDataFetcher;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class IDRDataFetcherStrategy {

    private final Map<String, IDRDataFetcher> strategies;

    public IDRDataFetcherStrategy(Map<String, IDRDataFetcher> strategies) {
        this.strategies = strategies;
    }

    public Object fetch(ResourceType type) {
        IDRDataFetcher fetcher = strategies.get(type.getPath());

        if (fetcher == null) {
            throw new IllegalArgumentException("Unknown resource type: " + type);
        }

        return fetcher.getData();
    }
}

