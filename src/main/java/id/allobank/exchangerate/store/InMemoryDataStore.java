package id.allobank.exchangerate.store;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class InMemoryDataStore {
    private Map<String, Object> data = Map.of();

    public synchronized void setData(Map<String, Object> newData) {
        this.data = Collections.unmodifiableMap(new HashMap<>(newData));
    }

    public Object get(String key) {
        return data.get(key);
    }
}
