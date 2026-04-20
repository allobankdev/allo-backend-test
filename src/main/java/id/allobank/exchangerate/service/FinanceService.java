package id.allobank.exchangerate.service;

import id.allobank.exchangerate.exception.ApiException;
import id.allobank.exchangerate.store.InMemoryDataStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FinanceService {

    private final InMemoryDataStore store;

    public Object getData(String type) {

        if (type == null || type.isBlank()) {
            throw new ApiException("resourceType cannot be empty");
        }

        Object data = store.get(type);

        if (data == null) {
            throw new ApiException("Invalid resourceType");
        }

        return data;
    }
}
