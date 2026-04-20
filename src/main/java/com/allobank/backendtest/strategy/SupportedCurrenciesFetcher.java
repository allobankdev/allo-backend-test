package com.allobank.backendtest.strategy;

import com.allobank.backendtest.constant.ApiConstants;
import com.allobank.backendtest.constant.MessageConstants;
import com.allobank.backendtest.constant.ResourceConstants;
import com.allobank.backendtest.exception.ExternalApiException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Component
public class SupportedCurrenciesFetcher implements IDRDataFetcher {
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public SupportedCurrenciesFetcher(WebClient webClient, ObjectMapper objectMapper) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
        log.info(MessageConstants.LOG_STRATEGY_REGISTERED, this.getClass().getSimpleName(), getResourceType());
    }

    @Override
    public String getResourceType() {
        return ResourceConstants.SUPPORTED_CURRENCIES;
    }

    @Override
    public Object fetchData() {
        log.info(MessageConstants.LOG_FETCH_START, getResourceType());

        try {
            String responseBody = webClient.get()
                    .uri(ApiConstants.CURRENCIES_PATH)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                            clientResponse -> clientResponse.bodyToMono(String.class)
                                    .map(body -> new ExternalApiException(
                                            MessageConstants.ERROR_EXTERNAL_API + " [" + clientResponse.statusCode() + "]: " + body)))
                    .bodyToMono(String.class)
                    .block();

            if (responseBody == null || responseBody.isBlank()) {
                throw new ExternalApiException(MessageConstants.ERROR_NULL_RESPONSE + " for " + getResourceType());
            }

            Map<String, String> currencies = objectMapper.readValue(
                    responseBody, new TypeReference<Map<String, String>>() {});

            log.info(MessageConstants.LOG_FETCH_SUCCESS, getResourceType());
            return Collections.unmodifiableMap(currencies);

        } catch (WebClientResponseException e) {
            throw new ExternalApiException(MessageConstants.ERROR_FETCH_CURRENCIES + ": HTTP " + e.getStatusCode(), e);
        } catch (ExternalApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ExternalApiException(MessageConstants.ERROR_FETCH_CURRENCIES + ": " + e.getMessage(), e);
        }
    }
}
