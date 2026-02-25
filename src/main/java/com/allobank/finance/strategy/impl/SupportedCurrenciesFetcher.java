package com.allobank.finance.strategy.impl;

import com.allobank.finance.exception.ExternalApiException;
import com.allobank.finance.model.FinanceDataResult;
import com.allobank.finance.strategy.IDRDataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Map;

// Todo : supported currencies fetcher
@Slf4j
@Component("supported_currencies")
public class SupportedCurrenciesFetcher implements IDRDataFetcher {

    private static final String RESOURCE_TYPE = "supported_currencies";

    @Override
    public String getResourceType() {
        return RESOURCE_TYPE;
    }

    @Override
    public List<FinanceDataResult> fetch(WebClient webClient) {
        log.info("Mengambil daftar mata uang yang didukung dari Frankfurter API...");

        try {
            Map<String, String> currencies = webClient.get()
                    .uri("/currencies")
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                            clientResponse -> clientResponse.bodyToMono(String.class)
                                    .map(body -> new ExternalApiException(
                                            "Frankfurter API error " + clientResponse.statusCode() + ": " + body)))
                    .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {
                    })
                    .block();

            if (currencies == null) {
                throw new ExternalApiException("Response kosong dari Frankfurter API /currencies");
            }

            log.info("Berhasil mengambil {} mata uang yang didukung", currencies.size());

            return List.of(new FinanceDataResult(RESOURCE_TYPE, currencies));

        } catch (WebClientResponseException e) {
            throw new ExternalApiException(
                    "Gagal mengambil daftar mata uang: " + e.getMessage(), e);
        } catch (ExternalApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ExternalApiException(
                    "Kesalahan tidak terduga saat mengambil daftar mata uang: " + e.getMessage(), e);
        }
    }
}
