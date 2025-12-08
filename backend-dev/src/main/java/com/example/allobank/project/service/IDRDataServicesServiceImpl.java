package com.example.allobank.project.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IDRDataServicesServiceImpl implements IDRDataServices {
	
	private final DataMemoryStore dataStore;
	
	@Override
    public Object getData(String resourceType) {
        Object data = dataStore.get(resourceType);

        if (data == null) {
            throw new IllegalArgumentException("Unknown resource type: " + resourceType);
        }
        return data;
    }
}
