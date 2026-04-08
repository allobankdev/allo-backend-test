package com.example.finance.service;

import org.springframework.stereotype.Service;

import com.example.finance.storage.InMemoryDataStore;

@Service
public class FinanceDataService {

	private final InMemoryDataStore store;

	public FinanceDataService(InMemoryDataStore store) {
		this.store = store;
	}

	public Object getData(String resourceType) {
		Object data = store.getData(resourceType);

		if (data == null) {
			throw new IllegalArgumentException("Invalid resource type: " + resourceType);
		}

		return data;
	}
}
