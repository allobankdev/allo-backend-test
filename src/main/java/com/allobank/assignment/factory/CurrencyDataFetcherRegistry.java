package com.allobank.assignment.factory;

import com.allobank.assignment.strategy.CurrencyDataFetcher;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CurrencyDataFetcherRegistry {

	private final Map<String, CurrencyDataFetcher> strategies;

	public CurrencyDataFetcherRegistry(List<CurrencyDataFetcher> fetchers) {
		this.strategies = new HashMap<>();

		for (CurrencyDataFetcher f : fetchers) {
			strategies.put(f.getName(), f);
		}
	}

	public CurrencyDataFetcher get(String name) {
		CurrencyDataFetcher fetcher = strategies.get(name);
		if (fetcher == null) {
			throw new IllegalArgumentException("Unknown strategy: " + name);
		}
		return fetcher;
	}
}

