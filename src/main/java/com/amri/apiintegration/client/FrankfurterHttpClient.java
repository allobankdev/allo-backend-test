package com.amri.apiintegration.client;

import com.amri.apiintegration.application.port.CurrencyRatesGateway;
import com.amri.apiintegration.dto.frankfurter.CurrenciesDto;
import com.amri.apiintegration.dto.frankfurter.HistoricalRatesDto;
import com.amri.apiintegration.dto.frankfurter.LatestRatesDto;
import com.amri.apiintegration.exception.ExternalApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class FrankfurterHttpClient implements CurrencyRatesGateway {

    private final RestClient restClient;

    @Override
    public LatestRatesDto getLatestRates(String base) {
        LatestRatesResponse response;
        try {
            response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/latest")
                            .queryParam("base", base)
                            .build())
                    .retrieve()
                    .body(LatestRatesResponse.class);
        } catch (RestClientResponseException e) {
            throw new ExternalApiException("Frankfurter latest rates error: HTTP " + e.getStatusCode(), e);
        } catch (RestClientException e) {
            throw new ExternalApiException("Frankfurter latest rates network error", e);
        }

        Objects.requireNonNull(response, "Failed to fetch latest rates from Frankfurter API");
        return new LatestRatesDto(response.base(), response.date(), response.rates(), null);
    }

    @Override
    public HistoricalRatesDto getHistoricalRates(String startDate, String endDate, String from, String to) {
        HistoricalRatesResponse response;
        try {
            response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/{range}")
                            .queryParam("from", from)
                            .queryParam("to", to)
                            .build(startDate + ".." + endDate))
                    .retrieve()
                    .body(HistoricalRatesResponse.class);
        } catch (RestClientResponseException e) {
            throw new ExternalApiException("Frankfurter historical rates error: HTTP " + e.getStatusCode(), e);
        } catch (RestClientException e) {
            throw new ExternalApiException("Frankfurter historical rates network error", e);
        }

        Objects.requireNonNull(response, "Failed to fetch historical rates from Frankfurter API");
        return new HistoricalRatesDto(response.amount(), response.base(), response.rates());
    }

    @Override
    public CurrenciesDto getCurrencies() {
        Map<String, String> response;
        try {
            response = restClient.get()
                    .uri("/currencies")
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });
        } catch (RestClientResponseException e) {
            throw new ExternalApiException("Frankfurter currencies error: HTTP " + e.getStatusCode(), e);
        } catch (RestClientException e) {
            throw new ExternalApiException("Frankfurter currencies network error", e);
        }

        Objects.requireNonNull(response, "Failed to fetch currencies from Frankfurter API");
        return new CurrenciesDto(response);
    }

    record LatestRatesResponse(String base, String date, Map<String, BigDecimal> rates) {
    }

    record HistoricalRatesResponse(BigDecimal amount, String base, Map<String, Map<String, BigDecimal>> rates) {
    }
}
