package com.allobank.assignment.strategy;

import java.util.concurrent.CompletableFuture;

public interface CurrencyDataFetcher {
	CompletableFuture<Object> fetch(String queryParam);

	String getName();
}
