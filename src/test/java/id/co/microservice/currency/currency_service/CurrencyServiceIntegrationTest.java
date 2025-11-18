package id.co.microservice.currency.currency_service;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CurrencyServiceIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testLatestIdrRatesEndpoint() {
        ResponseEntity<JsonNode> response =
                restTemplate.getForEntity("/api/finance/data/latest_idr_usd", JsonNode.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().has("USD_BuySpread_IDR")).isTrue();
    }

    @Test
    void testHistoricalIdrUsdEndpoint() {
        ResponseEntity<JsonNode> response =
                restTemplate.getForEntity("/api/finance/data/historical_idr_usd", JsonNode.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().has("rates")).isTrue();
    }

    @Test
    void testSupportedCurrenciesEndpoint() {
        ResponseEntity<JsonNode> response =
                restTemplate.getForEntity("/api/finance/data/supported_currencies", JsonNode.class);

        System.out.println(response.getBody());

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("currencies").isObject()).isTrue();
        assertThat(response.getBody().toString()).contains("USD");
    }

}
