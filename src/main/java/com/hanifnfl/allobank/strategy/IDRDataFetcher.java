package com.hanifnfl.allobank.strategy;

import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

public interface IDRDataFetcher {

    String getResourceTypeKey();

    void loadData(WebClient client);

    List<?> getCachedData();
}
