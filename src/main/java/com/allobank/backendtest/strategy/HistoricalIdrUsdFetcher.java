package com.allobank.backendtest.strategy;

import com.allobank.backendtest.constant.ApiConstants;
import com.allobank.backendtest.constant.CurrencyConstants;
import com.allobank.backendtest.constant.MessageConstants;
import com.allobank.backendtest.constant.ResourceConstants;
import com.allobank.backendtest.exception.ExternalApiException;
import com.allobank.backendtest.model.HistoricalRatesResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Component
public class HistoricalIdrUsdFetcher implements IDRDataFetcher {
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final String startDate;
    private final String endDate;

    public HistoricalIdrUsdFetcher(WebClient webClient, ObjectMapper objectMapper, @Value("${frankfurter.api.historical.start-date}") String startDate, @Value("${frankfurter.api.historical.end-date}") String endDate) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public String getResourceType() {
        return ResourceConstants.HISTORICAL_IDR_USD;
    }

    @Override
    public Object fetchData() {
        log.info(MessageConstants.LOG_FETCH_START, getResourceType());

        String path = "/" + startDate + ".." + endDate;
        String uri = UriComponentsBuilder.fromPath(path)
                .queryParam(ApiConstants.FROM_PARAM, CurrencyConstants.IDR)
                .queryParam(ApiConstants.TO_PARAM, CurrencyConstants.USD)
                .toUriString();

        try {
            String responseBody = webClient.get()
                    .uri(uri)
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

            HistoricalRatesResponse response = objectMapper.readValue(responseBody, HistoricalRatesResponse.class);

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("amount", response.amount());
            result.put("base", response.base());
            result.put("startDate", response.startDate());
            result.put("endDate", response.endDate());
            result.put("rates", response.rates());

            log.info(MessageConstants.LOG_FETCH_SUCCESS, getResourceType());
            return Collections.unmodifiableMap(result);

        } catch (WebClientResponseException e) {
            throw new ExternalApiException(MessageConstants.ERROR_FETCH_HISTORICAL + ": HTTP " + e.getStatusCode(), e);
        } catch (ExternalApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ExternalApiException(MessageConstants.ERROR_FETCH_HISTORICAL + ": " + e.getMessage(), e);
        }
    }
}
