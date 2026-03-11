package com.api.allorestapi.store;

import com.api.allorestapi.model.FinanceDataResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Component
public class FinanceDataStore {

    private final AtomicReference<Map<String, FinanceDataResponse>> storeRef =
            new AtomicReference<>(Collections.emptyMap());

    public void load(Map<String, FinanceDataResponse> data) {
        Map<String, FinanceDataResponse> immutable = Collections.unmodifiableMap(data);
        storeRef.set(immutable);
        log.info("In-memory store loaded with {} resource(s): {}", immutable.size(), immutable.keySet());
    }
    public FinanceDataResponse get(String resourceType) {
        return storeRef.get().get(resourceType);
    }

    public boolean isLoaded() {
        return !storeRef.get().isEmpty();
    }
}
