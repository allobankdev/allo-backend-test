package com.musfiul.idrrateaggregator.service.data.impl;

import com.musfiul.idrrateaggregator.constant.ResourceType;
import com.musfiul.idrrateaggregator.dto.api.APIResponseDTO;
import com.musfiul.idrrateaggregator.service.data.FinanceDataStoreService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FinanceDataStoreServiceImpl implements FinanceDataStoreService {
    private final Map<ResourceType, Object> store = new ConcurrentHashMap<>();

    @Override
    public void put(ResourceType type, Object data) {
        store.put(type, data);
    }

    @Override
    public APIResponseDTO get(ResourceType type) {
        Object object = store.get(type);
        return new APIResponseDTO(true, null, object, HttpStatus.OK);
    }
}
