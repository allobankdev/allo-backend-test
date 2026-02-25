package com.allobank.finance.strategy.impl;

import com.allobank.finance.config.FrankfurterProperties;
import com.allobank.finance.exception.ExternalApiException;
import com.allobank.finance.model.FinanceDataResult;
import com.allobank.finance.model.LatestRateResponse;
import com.allobank.finance.strategy.IDRDataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Map;

/**
 * Strategi untuk mengambil kurs IDR terbaru dari Frankfurter API
 * dan menghitung field tambahan USD_BuySpread_IDR.
 *
 * <p>
 * <b>Kalkulasi Spread Factor:</b>
 *
 * <pre>
 *   GitHub Username  : ramandry12
 *   Unicode Sum      : r(114)+a(97)+m(109)+a(97)+n(110)+d(100)+r(114)+y(121)+1(49)+2(50) = 961
 *   Spread Factor    : (961 % 1000) / 100000.0 = 961 / 100000.0 = 0.00961
 *   USD_BuySpread_IDR = (1 / Rate_USD) * (1 + 0.00961)
 * </pre>
 */
@Slf4j
@Component("latest_idr_rates")
public class LatestIdrRatesFetcher implements IDRDataFetcher {

    private static final String RESOURCE_TYPE = "latest_idr_rates";

    private final double spreadFactor;

    public LatestIdrRatesFetcher(FrankfurterProperties properties) {
        this.spreadFactor = calculateSpreadFactor(properties.getGithubUsername());
        log.info("LatestIdrRatesFetcher diinisialisasi dengan spreadFactor={} (dari username='{}')",
                spreadFactor, properties.getGithubUsername());
    }

    @Override
    public String getResourceType() {
        return RESOURCE_TYPE;
    }

    @Override
    public List<FinanceDataResult> fetch(WebClient webClient) {
        log.info("Mengambil latest IDR rates dari Frankfurter API...");
        try {
            LatestRateResponse response = webClient.get()
                    .uri("/latest?base=IDR")
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                            clientResponse -> clientResponse.bodyToMono(String.class)
                                    .map(body -> new ExternalApiException(
                                            "Frankfurter API error " + clientResponse.statusCode() + ": " + body)))
                    .bodyToMono(LatestRateResponse.class)
                    .block();

            if (response == null) {
                throw new ExternalApiException("Response kosong dari Frankfurter API /latest");
            }

            // Hitung USD_BuySpread_IDR
            Map<String, Double> rates = response.getRates();
            if (rates != null && rates.containsKey("USD")) {
                double rateUsd = rates.get("USD");
                double usdBuySpreadIdr = (1.0 / rateUsd) * (1.0 + spreadFactor);
                response.setUsdBuySpreadIdr(usdBuySpreadIdr);
                log.info("USD BuySpread IDR dihitung: rate_USD={}, spreadFactor={}, result={}",
                        rateUsd, spreadFactor, usdBuySpreadIdr);
            } else {
                log.warn("Rate USD tidak ditemukan dalam respons API");
            }

            return List.of(new FinanceDataResult(RESOURCE_TYPE, response));

        } catch (WebClientResponseException e) {
            throw new ExternalApiException(
                    "Gagal mengambil latest IDR rates: " + e.getMessage(), e);
        } catch (ExternalApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ExternalApiException(
                    "Kesalahan tidak terduga saat mengambil latest IDR rates: " + e.getMessage(), e);
        }
    }

    /**
     * Menghitung spread factor berdasarkan GitHub username.
     *
     * <p>
     * Formula:
     * <ol>
     * <li>Hitung jumlah nilai Unicode dari semua karakter username (lowercase)</li>
     * <li>Spread Factor = (sum % 1000) / 100000.0</li>
     * </ol>
     *
     * @param githubUsername username GitHub
     * @return spread factor dalam rentang [0.00000, 0.00999]
     */
    public static double calculateSpreadFactor(String githubUsername) {
        if (githubUsername == null || githubUsername.isBlank()) {
            throw new IllegalArgumentException("GitHub username tidak boleh kosong");
        }
        int unicodeSum = githubUsername.toLowerCase().chars().sum();
        return (unicodeSum % 1000) / 100000.0;
    }
}
