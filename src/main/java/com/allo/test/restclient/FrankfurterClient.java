package com.allo.test.restclient;

import com.allo.test.config.FrankfurterProperties;
import com.allo.test.dto.response.HistoricalIdrUsdResponse;
import com.allo.test.dto.response.LatestIDRRatesResponse;
import lombok.extern.slf4j.Slf4j; // Disarankan pakai Lombok untuk logging
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j // Mengaktifkan logging
@Service
public class FrankfurterClient {

    private final RestTemplate frankfurtRestTemplate;
    private final FrankfurterProperties properties;

    public FrankfurterClient(RestTemplate frankfurtRestTemplate,
                             FrankfurterProperties properties) {
        this.frankfurtRestTemplate = frankfurtRestTemplate;
        this.properties = properties;
    }

    public LatestIDRRatesResponse getLatestRates() {
        return executeApi(
                properties.getEndpoints().getLatest(),
                new ParameterizedTypeReference<LatestIDRRatesResponse>() {}
        );
    }

    public HistoricalIdrUsdResponse getHistorical() {
        return executeApi(
                properties.getEndpoints().getHistorical(),
                new ParameterizedTypeReference<HistoricalIdrUsdResponse>() {}
        );
    }

    public Map<String, String> getSupportedCurrency() {
        return executeApi(
                properties.getEndpoints().getCurrencies(),
                new ParameterizedTypeReference<Map<String, String>>() {}
        );
    }



    private <T> T executeApi(String endpoint, ParameterizedTypeReference<T> responseType) {
//        String fullUrl = properties.getBaseUrl().concat(endpoint);

        try {
            return frankfurtRestTemplate.exchange(
                    endpoint,
                    HttpMethod.GET,
                    null,
                    responseType
            ).getBody();

        } catch (RestClientException e) {

            log.error("Gagal memanggil API Frankfurter di URL: {}. Error: {}", endpoint, e.getMessage());

            throw new RuntimeException("Terjadi kesalahan saat komunikasi dengan Third Party API", e);
        }
    }
}