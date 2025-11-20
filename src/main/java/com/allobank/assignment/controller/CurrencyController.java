package com.allobank.assignment.controller;

import com.allobank.assignment.store.CurrencyDataStore;
import com.allobank.assignment.strategy.CurrencyDataFetcher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/currency/data")
public class CurrencyController {

	private final CurrencyDataStore store;
	private final Map<String, CurrencyDataFetcher> fetcherMap;

	public CurrencyController(CurrencyDataStore store, Map<String, CurrencyDataFetcher> fetcherMap) {
		this.store = store;
		this.fetcherMap = fetcherMap;
	}

	@GetMapping("/{resourceType}")
	public ResponseEntity<Object> getData(@PathVariable String resourceType) throws Exception {
		CurrencyDataFetcher fetcher = fetcherMap.get(resourceType);
		if (fetcher == null) {
			return ResponseEntity.notFound().build();
		}
		Object data = fetcher.fetch(null).get();
		return ResponseEntity.ok(data);
	}
}
