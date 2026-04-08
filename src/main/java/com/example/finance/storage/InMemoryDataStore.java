package com.example.finance.storage;

import java.util.Collections;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class InMemoryDataStore {

	private Map<String, Object> data;

	public void setData(Map<String, Object> data) {
		this.data = Collections.unmodifiableMap(data);
	}

	public Map<String, Object> getAllData() {
		return data;
	}

	public Object getData(String key) {
		return data.get(key);
	}
}
