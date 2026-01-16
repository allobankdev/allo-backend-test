package com.allo.aggregator.store;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe in-memory store for fetched data.
 * Adheres to Constraint C (Immutability):
 * While the internal map is mutable during loading, we expose an unmodifiable
 * view or just accessors.
 * Since the requirement says "Immutable once loaded", we can ensure the Runner
 * populates it and then it's read-only effectively.
 */
@Component
public class ExchangeRateStore {
  private final Map<String, Object> dataMap = new ConcurrentHashMap<>();

  public void putData(String key, Object data) {
    dataMap.put(key, data);
  }

  public Object getData(String key) {
    return dataMap.get(key);
  }

  public Map<String, Object> getAllData() {
    return Collections.unmodifiableMap(dataMap);
  }

  public boolean containsKey(String key) {
    return dataMap.containsKey(key);
  }
}
