package id.allobank.exchangerate.service;

import id.allobank.exchangerate.exception.ApiException;
import id.allobank.exchangerate.store.InMemoryDataStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FinanceService {

    private final InMemoryDataStore store;
    private final StrategyRegistry strategyRegistry;

    public Object getData(String type) {

        strategyRegistry.get(type);

        Object data = store.get(type);

        if (data == null) {
            throw new ApiException("Invalid resourceType");
        }

        if (data instanceof List<?> list) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> normalized = (List<Map<String, Object>>) list;
            return normalized;
        }

        if (data instanceof Map<?, ?> map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> normalized = (Map<String, Object>) map;
            return List.of(normalized);
        }

        throw new ApiException("Unsupported payload type");
    }
}
