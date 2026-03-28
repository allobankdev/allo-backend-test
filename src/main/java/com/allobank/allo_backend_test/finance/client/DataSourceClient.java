package com.allobank.allo_backend_test.finance.client;

import com.allobank.allo_backend_test.finance.exception.DataSourceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSourceClient {

    private final RestClient restClient;

    public <T> T get(String path, Class<T> responseType) {
        log.info("GET path={}", path);
        try {
            T result = restClient.get()
                    .uri(path)
                    .retrieve()
                    .body(responseType);
            log.info("GET path={} status=200 response={}", path, result);
            return result;
        } catch (Exception e) {
            log.error("GET path={} error={}", path, e.getMessage());
            throw new DataSourceException("Failed to fetch from " + path);
        }
    }

    public <T> T getWithParams(String path, Map<String, String> params, Class<T> responseType) {
        log.info("GET path={} params={}", path, params);
        try {
            T result = restClient.get()
                    .uri(u -> {
                        var builder = u.path(path);
                        params.forEach(builder::queryParam);
                        return builder.build();
                    })
                    .retrieve()
                    .body(responseType);
            log.info("GET path={} params={} status=200 response={}", path, params, result);
            return result;
        } catch (Exception e) {
            log.error("GET path={} params={} error={}", path, params, e.getMessage());
            throw new DataSourceException("Failed to fetch from " + path);
        }
    }
}