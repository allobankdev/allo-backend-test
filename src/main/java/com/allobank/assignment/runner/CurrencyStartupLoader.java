package com.allobank.assignment.runner;

import com.allobank.assignment.factory.CurrencyDataFetcherRegistry;
import com.allobank.assignment.store.CurrencyDataStore;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class CurrencyStartupLoader implements ApplicationRunner {

	private final CurrencyDataFetcherRegistry registry;
	private final CurrencyDataStore store;

	public CurrencyStartupLoader(CurrencyDataFetcherRegistry registry, CurrencyDataStore store) {
		this.registry = registry;
		this.store = store;
	}

	@Override
	public void run(ApplicationArguments args) {

		CompletableFuture<Object> latestFuture = registry.get("latest_idr_rates").fetch(null);

		CompletableFuture<Object> historicalFuture = registry.get("historical_idr_usd").fetch(null);

		CompletableFuture<Object> currenciesFuture = registry.get("supported_currencies").fetch(null);

		List<Object> results = CompletableFuture.allOf(latestFuture, historicalFuture, currenciesFuture)
				.thenApply(v -> List.of(latestFuture.join(), historicalFuture.join(), currenciesFuture.join())).join();

		store.put("data", results);
		
		store.lock();
	}
}

