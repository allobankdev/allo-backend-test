package allobankdev.test.finance.service;

import allobankdev.test.finance.exception.InvalidResourceTypeException;
import allobankdev.test.finance.store.FinanceDataStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinanceService {

    private final FinanceDataStore store;

    public FinanceService(FinanceDataStore store) {
        this.store = store;
    }

    public List<Object> getData(String resourceType) {
        Object data = store.get(resourceType);

        if (data == null) {
            throw new InvalidResourceTypeException(resourceType);
        }

        return List.of(store.get(resourceType));
    }
}

