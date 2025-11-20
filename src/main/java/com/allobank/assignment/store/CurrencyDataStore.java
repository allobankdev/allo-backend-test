package com.allobank.assignment.store;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CurrencyDataStore {

	private final Map<String, Object> store = new ConcurrentHashMap<String, Object>() {
	};
	private volatile boolean locked = false;

	public void put(String key, Object data) {
		if (locked) {
			throw new IllegalArgumentException("DataStore is locked and immutable");
		}
		store.put(key, data);
	}

	public Object get(String key) {
		return store.get(key);
	}

	public void lock() {
		locked = true;
	}

	public Map<String, Object> getAll() {
		return Collections.unmodifiableMap(store);
	}
}
