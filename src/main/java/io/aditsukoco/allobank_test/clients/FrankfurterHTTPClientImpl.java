package io.aditsukoco.allobank_test.clients;

import io.aditsukoco.allobank_test.models.dto.LatestAPIResponseDTO;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@NoArgsConstructor
public class FrankfurterHTTPClientImpl implements FrankfurterHTTPClientInterface {

    @Value("frankfurter.base_url")
    private String FRANKFURTER_BASE_URL;

    private final RestClient REST_CLIENT = RestClient.builder().baseUrl(FRANKFURTER_BASE_URL).build();


    @Override
    public LatestAPIResponseDTO fetchLatest(float amount, String currencyFrom, String currencyTo) throws RestClientException {
        return REST_CLIENT.get()
                .uri("/latest?base="+currencyFrom+"&to="+currencyTo+"&amount="+amount)
                .retrieve()
                .body(LatestAPIResponseDTO.class);
    }

}
