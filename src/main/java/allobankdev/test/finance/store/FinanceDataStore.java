package allobankdev.test.finance.store;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class FinanceDataStore {

    private Map<String, Object> data;

    public synchronized void load(Map<String, Object> loaded) {
        this.data = Collections.unmodifiableMap(loaded);
    }

    public Object get(String key) {
        return data.get(key);
    }
}

