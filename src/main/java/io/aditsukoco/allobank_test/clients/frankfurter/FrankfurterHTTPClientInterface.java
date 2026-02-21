package io.aditsukoco.allobank_test.clients.frankfurter;

import io.aditsukoco.allobank_test.models.dto.api_response.LatestAPIResponseDTO;
import org.springframework.web.client.RestClientException;

public interface FrankfurterHTTPClientInterface {
    public LatestAPIResponseDTO fetchLatest(float amount, String currencyFrom, String currencyTo) throws RestClientException;
}
