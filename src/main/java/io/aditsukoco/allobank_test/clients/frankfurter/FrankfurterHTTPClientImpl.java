package io.aditsukoco.allobank_test.clients.frankfurter;

import io.aditsukoco.allobank_test.models.dto.api_response.HistoricalDataAPIResponseDTO;
import io.aditsukoco.allobank_test.models.dto.api_response.LatestAPIResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class FrankfurterHTTPClientImpl implements FrankfurterHTTPClientInterface {
    private String frankfurterBaseUrl;
    private RestTemplate restTemplate;

    public FrankfurterHTTPClientImpl(String frankfurterBaseUrl, RestTemplate restTemplate) {
        this.frankfurterBaseUrl = frankfurterBaseUrl;
        this.restTemplate = restTemplate;
    }

    @Override
    public LatestAPIResponseDTO fetchLatest(float amount, String currencyFrom, String currencyTo) throws RestClientException {
        URI uri = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(this.frankfurterBaseUrl)
                .path("/latest")
                .queryParam("base", currencyFrom)
                .queryParam("to", currencyTo)
                .queryParam("amount", amount)
                .build()
                .toUri();
        return restTemplate.getForObject(uri, LatestAPIResponseDTO.class);
    }

    @Override
    public HistoricalDataAPIResponseDTO fetchHistorical(String fromCurrency, String toCurrency, String startDate, String endDate) throws RestClientException {
        URI uri = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(this.frankfurterBaseUrl)
                .path("/" + startDate + ".." + endDate)
                .queryParam("from", fromCurrency)
                .queryParam("to", toCurrency)
                .build()
                .toUri();
        return restTemplate.getForObject(uri, HistoricalDataAPIResponseDTO.class);
    }

    @Override
    public Map<String, String> fetchCurrencies() throws RestClientException {
        URI uri = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(this.frankfurterBaseUrl)
                .path("/currencies")
                .build()
                .toUri();
        return restTemplate.getForObject(uri, Map.class);
    }
}
