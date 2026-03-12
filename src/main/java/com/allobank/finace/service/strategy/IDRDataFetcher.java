package com.allobank.finace.service.strategy;

import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

public interface IDRDataFetcher {

    String getResourceType();

    List<Map<String, Object>> fetch(WebClient webClient);
}
