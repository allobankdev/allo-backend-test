package com.example.allobank.project.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.allobank.project.strategy.IDRDataFetcher;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IDRDataServicesServiceImpl implements IDRDataServices {
	
	private final Map<String, IDRDataFetcher> fetcherStrategies;	
	
	@Override
    public Object getData(String resourceType) {
        IDRDataFetcher fetcher = fetcherStrategies.get(resourceType);

        if (fetcher == null) {
            throw new IllegalArgumentException("Unknown resource type: " + resourceType);
        }

        return fetcher.fetchData();
    }
}
