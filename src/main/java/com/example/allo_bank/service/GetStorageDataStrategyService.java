package com.example.allo_bank.service;

import com.example.allo_bank.dto.ApiResponse;
import com.example.allo_bank.util.TypeEnum;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GetStorageDataStrategyService {

    private final Map<String, GetStorageDataService> strategies;

    public GetStorageDataStrategyService(Map<String, GetStorageDataService> strategies) {
        this.strategies = strategies;
    }

    public ApiResponse<Object> getData(TypeEnum type) {

        GetStorageDataService getStorageDataService = strategies.get(type.getPath());

        if (getStorageDataService == null) {
            throw new IllegalArgumentException("Unknown resource type: " + type);
        }

        return getStorageDataService.fetchData();
    }

}
