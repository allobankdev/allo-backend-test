package id.allobank.exchangerate.store;

import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class InMemoryDataStore {

    private volatile Map<String, Object> data = Map.of();

    public synchronized void setAll(Map<String, Object> newData){
        Map<String, Object> snapshot = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : newData.entrySet()) {
            snapshot.put(entry.getKey(), deepImmutableCopy(entry.getValue()));
        }
        this.data = Collections.unmodifiableMap(snapshot);
    }

    public Object get(String key){
        return data.get(key);
    }

    private Object deepImmutableCopy(Object value) {
        if (value == null || isScalarValue(value)) {
            return value;
        }

        if (value instanceof Map<?, ?> mapValue) {
            Map<String, Object> copied = new LinkedHashMap<>();
            for (Map.Entry<?, ?> entry : mapValue.entrySet()) {
                copied.put(String.valueOf(entry.getKey()), deepImmutableCopy(entry.getValue()));
            }
            return Collections.unmodifiableMap(copied);
        }

        if (value instanceof List<?> listValue) {
            List<Object> copied = new ArrayList<>(listValue.size());
            for (Object item : listValue) {
                copied.add(deepImmutableCopy(item));
            }
            return Collections.unmodifiableList(copied);
        }

        if (value instanceof Set<?> setValue) {
            Set<Object> copied = new LinkedHashSet<>();
            for (Object item : setValue) {
                copied.add(deepImmutableCopy(item));
            }
            return Collections.unmodifiableSet(copied);
        }

        if (value.getClass().isArray()) {
            int length = Array.getLength(value);
            List<Object> copied = new ArrayList<>(length);
            for (int i = 0; i < length; i++) {
                copied.add(deepImmutableCopy(Array.get(value, i)));
            }
            return Collections.unmodifiableList(copied);
        }

        return value;
    }

    private boolean isScalarValue(Object value) {
        return value instanceof String
                || value instanceof Number
                || value instanceof Boolean
                || value instanceof Enum<?>;
    }
}
