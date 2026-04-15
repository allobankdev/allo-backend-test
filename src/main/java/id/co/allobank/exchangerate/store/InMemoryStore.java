package id.co.allobank.exchangerate.store;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class InMemoryStore {

    private Map<String, Object> data;

    public void init(Map<String, Object> data) {
        this.data = Map.copyOf(data); // immutable
    }

    public Object get(String key) {
        Object result = data.get(key);
        return result;
    }
}