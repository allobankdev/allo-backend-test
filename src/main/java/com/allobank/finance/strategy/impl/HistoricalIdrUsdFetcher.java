package com.allobank.finance.strategy.impl;

import com.allobank.finance.config.FrankfurterProperties;
import com.allobank.finance.exception.ExternalApiException;
import com.allobank.finance.model.FinanceDataResult;
import com.allobank.finance.model.HistoricalRateResponse;
import com.allobank.finance.strategy.IDRDataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

/**
 * Strategi untuk mengambil data historis kurs IDR→USD
 * pada rentang tanggal yang dikonfigurasi di application.yml.
 *
 * <p>
 * Endpoint yang dipanggil: /{start}..{end}?from=IDR&to=USD
 */
@Slf4j
@Component("historical_idr_usd")
public class HistoricalIdrUsdFetcher implements IDRDataFetcher {

    private static final String RESOURCE_TYPE = "historical_idr_usd";

    private final FrankfurterProperties properties;

    public HistoricalIdrUsdFetcher(FrankfurterProperties properties) {
        this.properties = properties;
    }

    @Override
    public String getResourceType() {
        return RESOURCE_TYPE;
    }

    @Override
    public List<FinanceDataResult> fetch(WebClient webClient) {
        String startDate = properties.getHistoricalStart();
        String endDate = properties.getHistoricalEnd();
        String uri = "/" + startDate + ".." + endDate + "?from=IDR&to=USD";

        log.info("Mengambil data historis IDR→USD dari Frankfurter API: {}", uri);

        try {
            HistoricalRateResponse response = webClient.get()
                    .uri(uri)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                            clientResponse -> clientResponse.bodyToMono(String.class)
                                    .map(body -> new ExternalApiException(
                                            "Frankfurter API error " + clientResponse.statusCode() + ": " + body)))
                    .bodyToMono(HistoricalRateResponse.class)
                    .block();

            if (response == null) {
                throw new ExternalApiException("Response kosong dari Frankfurter API " + uri);
            }

            log.info("Data historis IDR→USD berhasil diambil: {} entri tanggal",
                    response.getRates() != null ? response.getRates().size() : 0);

            return List.of(new FinanceDataResult(RESOURCE_TYPE, response));

        } catch (WebClientResponseException e) {
            throw new ExternalApiException(
                    "Gagal mengambil data historis IDR→USD: " + e.getMessage(), e);
        } catch (ExternalApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ExternalApiException(
                    "Kesalahan tidak terduga saat mengambil data historis: " + e.getMessage(), e);
        }
    }
}
