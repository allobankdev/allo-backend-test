package io.aditsukoco.allobank_test.clients.frankfurter;

import io.aditsukoco.allobank_test.models.dto.api_response.HistoricalDataAPIResponseDTO;
import io.aditsukoco.allobank_test.models.dto.api_response.LatestAPIResponseDTO;
import org.springframework.web.client.RestClientException;

import java.util.Map;

public interface FrankfurterHTTPClientInterface {
    LatestAPIResponseDTO fetchLatest(float amount, String currencyFrom, String currencyTo) throws RestClientException;
    HistoricalDataAPIResponseDTO fetchHistorical(String fromCurrency, String toCurrency, String startDate, String endDate) throws RestClientException;
    Map<String, String> fetchCurrencies() throws RestClientException;
}
