package io.aditsukoco.allobank_test.clients.frankfurter;

import io.aditsukoco.allobank_test.models.dto.api_response.LatestAPIResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

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

}
