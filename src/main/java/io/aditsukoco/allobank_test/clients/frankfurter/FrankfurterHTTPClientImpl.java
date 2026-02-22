package io.aditsukoco.allobank_test.clients.frankfurter;

import io.aditsukoco.allobank_test.models.dto.api_response.HistoricalDataAPIResponseDTO;
import io.aditsukoco.allobank_test.models.dto.api_response.LatestAPIResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class FrankfurterHTTPClientImpl implements FrankfurterHTTPClientInterface {
    private RestClient restClient;

    public FrankfurterHTTPClientImpl(String frankfurterBaseUrl) {
        this.restClient = RestClient.builder().baseUrl(frankfurterBaseUrl).build();
    }

    @Override
    public LatestAPIResponseDTO fetchLatest(float amount, String currencyFrom, String currencyTo) throws RestClientException {
        String uri = "/latest?base="+currencyFrom+"&to="+currencyTo+"&amount="+amount;
        return restClient.get()
                .uri(uri)
                .retrieve()
                .body(LatestAPIResponseDTO.class);
    }

    @Override
    public HistoricalDataAPIResponseDTO fetchHistorical(String fromCurrency, String toCurrency, String startDate, String endDate) throws RestClientException {
        String uri = "/"+startDate+".."+endDate+"?from="+fromCurrency+"&to="+toCurrency;
        return restClient.get()
                .uri(uri)
                .retrieve()
                .body(HistoricalDataAPIResponseDTO.class);
    }

    @Override
    public Map<String, String> fetchCurrencies() throws RestClientException {
        String uri = "/currencies";
        return restClient.get()
                .uri(uri)
                .retrieve()
                .body(Map.class);
    }

}
