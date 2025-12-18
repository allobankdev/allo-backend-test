package com.allo.backendtest.service.impl;

import com.allo.backendtest.dto.frankfurter.CurrenciesDto;
import com.allo.backendtest.service.IdrDataFetcher;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service("supported_currencies")
public class SupportedCurrenciesService implements IdrDataFetcher {

    private final CompletableFuture<CurrenciesDto> completable = new CompletableFuture<>();

    private static final String PATH_CURRENCIES = "/currencies";

    private final RestClient restClient;
    private final ObjectMapper mapper;

    public SupportedCurrenciesService(RestClient restClient, ObjectMapper mapper) {
        this.restClient = restClient;
        this.mapper = mapper;
    }

    @Override
    public void update() throws Exception {
        completable.complete(getData());
    }

    @Override
    public Object fetch() {
        return completable.join();
    }

    public CurrenciesDto getData() throws Exception {
        Map<String, String> result = restClient.get()
                .uri(PATH_CURRENCIES)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        if(result == null) {
            throw new Exception("Failed : result not found");
        }

        Map<String, Map<?, ?>> wrapper = Map.of("mapCurrencies", result);
        return mapper.convertValue(wrapper, CurrenciesDto.class);
    }
}
