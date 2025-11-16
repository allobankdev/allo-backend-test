package com.allobank.service;

import com.allobank.config.FrankfurterApiProperties;
import com.allobank.enums.ResourceType;
import com.allobank.store.DataStoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component("supported_currencies")
@RequiredArgsConstructor
@Slf4j
public class SupportedCurrenciesStrategy implements IDRDataFetcher {

    private final WebClient webClient;
    private final FrankfurterApiProperties properties;
    private final DataStoreService dataStoreService;

    @Override
    public Object fetchFromExternalApi() {
       return null;
    }

    @Override
    public Object getData() {
        return null;
    }

    @Override
    public ResourceType getResourceType() {
        return null;
    }
}
