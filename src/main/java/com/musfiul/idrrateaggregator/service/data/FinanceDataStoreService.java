package com.musfiul.idrrateaggregator.service.data;

import com.musfiul.idrrateaggregator.constant.ResourceType;
import com.musfiul.idrrateaggregator.dto.api.APIResponseDTO;

public interface FinanceDataStoreService {
    void put(ResourceType type, Object data);

    APIResponseDTO get(ResourceType type);
}
