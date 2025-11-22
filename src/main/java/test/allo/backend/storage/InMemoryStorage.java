package test.allo.backend.storage;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class InMemoryStorage {
    private Map<String, JsonNode> data = Map.of();
    boolean locked = false;

    public synchronized void save(String key, JsonNode value) {
        if (locked) {
            throw new IllegalStateException("storage data modification is prohibited");
        }

        Map<String, JsonNode> copy = new HashMap<>(data);
        copy.put(key, value.deepCopy());
        data = Map.copyOf(copy);
    }

    public synchronized void lockStorage() {
        locked = true;
    }

    public JsonNode get(String key) {
        JsonNode stored = data.get(key);
        return stored == null ? null : stored.deepCopy();
    }
}
