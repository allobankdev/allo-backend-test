package com.allobank.allo_backend_test.finance.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSourceClient {
    private final RestClient restClient;

    public <T> T get(String path, Class<T> responseType) {
        return restClient.get()
                .uri(path)
                .retrieve()
                .body(responseType);
    }

    public <T> T getWithParams(String path, Map<String, String> params, Class<T> responseType) {
        return restClient.get()
                .uri(u -> {
                    var builder = u.path(path);
                    params.forEach(builder::queryParam);
                    return builder.build();
                })
                .retrieve()
                .body(responseType);
    }
}