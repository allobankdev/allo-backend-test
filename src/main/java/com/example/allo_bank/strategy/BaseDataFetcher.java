package com.example.allo_bank.strategy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public abstract class BaseDataFetcher implements IDRDataFetcher {

    protected final RestTemplate restTemplate;

    @Value("${frankfurter.api.base-url}")
    protected final String baseUrl;

    protected BaseDataFetcher(RestTemplate restTemplate, String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    protected RestTemplate getRestTemplate() {
        return this.restTemplate;
    }

    protected String getBaseUrl() {
        return this.baseUrl;
    }

    public abstract String getResourceName();

    public abstract Object fetchData();

    @Override
    public Object safeFetch() {
        try {
            Object data = fetchData();
            return success(data);
        }
        catch (ResourceAccessException e) {
            return error("98", "network error while calling external API");
        }
        catch (HttpClientErrorException | HttpServerErrorException e) {
            return error("97", "external API returned: " + e.getStatusCode());
        }
        catch (Exception e) {
            return error("96", "unexpected internal error");
        }
    }

    private Map<String, Object> success(Object data) {
        return Map.of(
                "code", "00",
                "status", "success",
                "message", "ok",
                "data", data
        );
    }

    private Map<String, Object> error(String code, String message) {
        return Map.of(
                "code", code,
                "status", "failed",
                "message", message,
                "data", null
        );
    }
}
