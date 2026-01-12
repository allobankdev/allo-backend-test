package id.co.evan.project.aggregator.service;

import id.co.evan.project.aggregator.model.response.UnifiedFinanceResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class DataStoreService {

    private final Map<String, UnifiedFinanceResponse> mutableStorage = new ConcurrentHashMap<>();
    private final AtomicReference<Map<String, UnifiedFinanceResponse>> immutableStorage = new AtomicReference<>(Collections.emptyMap());
    private final AtomicBoolean sealed = new AtomicBoolean(false);

    public void putData(String resourceType, UnifiedFinanceResponse data) {
        if (sealed.get()) {
            throw new IllegalStateException("DataStore is sealed. No more writes allowed.");
        }
        mutableStorage.put(resourceType, data);
    }

    public void finalizeStore() {
        if (sealed.compareAndSet(false, true)) {
            immutableStorage.set(Map.copyOf(mutableStorage));
        }
    }

    public Mono<UnifiedFinanceResponse> getData(String resourceType) {
        return Mono.justOrEmpty(immutableStorage.get().get(resourceType));
    }

    public boolean isReady() {
        return sealed.get();
    }
}