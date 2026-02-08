package com.allobank.test.strategy;

import java.util.concurrent.CompletableFuture;

public interface IdrDataFetcher {
    CompletableFuture<?> fetchData();
    String getResourceType();
}