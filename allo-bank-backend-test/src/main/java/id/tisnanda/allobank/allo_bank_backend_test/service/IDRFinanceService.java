package id.tisnanda.allobank.allo_bank_backend_test.service;


import id.tisnanda.allobank.allo_bank_backend_test.exception.ResourceNotFoundException;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class IDRFinanceService {

    private static final Logger log = Logger.getLogger(IDRFinanceService.class);

    private final Map<String, List<Map<String, Object>>> dataStore = new ConcurrentHashMap<>();

    public void setData(String resourceType, List<Map<String, Object>> data) {
        dataStore.put(resourceType, Collections.unmodifiableList(data));
    }

    public List<Map<String, Object>> getData(String resourceType) {
        List<Map<String, Object>> data = dataStore.get(resourceType);
        if (data == null) {
            throw new ResourceNotFoundException(resourceType);
        }
        return dataStore.getOrDefault(resourceType, Collections.emptyList());
    }

}
