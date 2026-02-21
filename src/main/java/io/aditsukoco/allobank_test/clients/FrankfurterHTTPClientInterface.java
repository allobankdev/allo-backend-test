package io.aditsukoco.allobank_test.clients;

import io.aditsukoco.allobank_test.models.dto.LatestAPIResponseDTO;
import org.springframework.web.client.RestClientException;

public interface FrankfurterHTTPClientInterface {
    public LatestAPIResponseDTO fetchLatest(float amount, String currencyFrom, String currencyTo) throws RestClientException;
}
