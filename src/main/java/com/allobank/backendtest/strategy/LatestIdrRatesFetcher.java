package com.allobank.backendtest.strategy;

import com.allobank.backendtest.constant.ApiConstants;
import com.allobank.backendtest.constant.CurrencyConstants;
import com.allobank.backendtest.constant.MessageConstants;
import com.allobank.backendtest.constant.ResourceConstants;
import com.allobank.backendtest.exception.ExternalApiException;
import com.allobank.backendtest.model.LatestRatesResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
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
public class LatestIdrRatesFetcher implements IDRDataFetcher {
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final String githubUsername;

    @Getter
    private final double spreadFactor;

    public LatestIdrRatesFetcher(WebClient webClient, ObjectMapper objectMapper, @Value("${spread.github-username}") String githubUsername) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
        this.githubUsername = githubUsername.toLowerCase();
        this.spreadFactor = calculateSpreadFactor(this.githubUsername);
        
        log.info(MessageConstants.LOG_STRATEGY_REGISTERED, this.getClass().getSimpleName(), getResourceType());
    }

    @Override
    public String getResourceType() {
        return ResourceConstants.LATEST_IDR_RATES;
    }

    @Override
    public Object fetchData() {
        log.info(MessageConstants.LOG_FETCH_START, getResourceType());

        String uri = UriComponentsBuilder.fromPath(ApiConstants.LATEST_PATH)
                .queryParam(ApiConstants.BASE_PARAM, CurrencyConstants.IDR)
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

            LatestRatesResponse response = objectMapper.readValue(responseBody, LatestRatesResponse.class);
            if (response.rates() == null) {
                throw new ExternalApiException("Rates field is null in the response");
            }

            return transformResponse(response);

        } catch (WebClientResponseException e) {
            throw new ExternalApiException(MessageConstants.ERROR_FETCH_LATEST + ": HTTP " + e.getStatusCode(), e);
        } catch (ExternalApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ExternalApiException(MessageConstants.ERROR_FETCH_LATEST + ": " + e.getMessage(), e);
        }
    }

    private Map<String, Object> transformResponse(LatestRatesResponse response) {
        Double rateUsd = response.rates().get(CurrencyConstants.USD);
        if (rateUsd == null || rateUsd == 0.0) {
            throw new ExternalApiException("USD rate not found or is zero in the API response");
        }

        double usdBuySpreadIdr = (1.0 / rateUsd) * (1.0 + spreadFactor);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("amount", response.amount());
        result.put("base", response.base());
        result.put("date", response.date());
        result.put("rates", response.rates());
        
        result.put("githubUsername", githubUsername);
        result.put("spreadFactor", spreadFactor);
        result.put("USD_BuySpread_IDR", usdBuySpreadIdr);

        return Collections.unmodifiableMap(result);
    }

    static double calculateSpreadFactor(String username) {
        int sum = 0;
        for (char c : username.toCharArray()) {
            sum += c;
        }
        return (sum % 1000) / 100000.0;
    }
}
