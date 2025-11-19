package com.athallah.finance.service;

import com.athallah.finance.startegy.IDRDataFetcher;
import com.athallah.finance.util.constant.ResourceType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class FinanceService {

    private final FinanceDataStore dataStore;

    public Object getData(ResourceType resourceType) {
        Object data = dataStore.get(resourceType);

        if (data == null) {
            throw new IllegalArgumentException(
                    "Data for resourceType " + resourceType + " is not available"
            );
        }

        return data;
    }
}

