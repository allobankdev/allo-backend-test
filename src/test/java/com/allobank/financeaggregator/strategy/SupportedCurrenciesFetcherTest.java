package com.allobank.financeaggregator.strategy;

import static org.assertj.core.api.Assertions.assertThat;

import com.allobank.financeaggregator.dto.SupportedCurrenciesDto;
import com.allobank.financeaggregator.service.FrankfurterClient;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;

class SupportedCurrenciesFetcherTest {

    @Test
    void fetchDataReturnsCurrencyMap() {
        Map<String, String> response = Map.of("USD", "United States Dollar", "IDR", "Indonesian Rupiah");
        StubFrankfurterClient client = new StubFrankfurterClient(response);

        SupportedCurrenciesFetcher fetcher = new SupportedCurrenciesFetcher(client);
        SupportedCurrenciesDto payload = fetcher.fetchData();

        assertThat(client.lastPath()).isEqualTo("/currencies");
        assertThat(payload.currencies().get("USD")).isEqualTo("United States Dollar");
    }

    private static class StubFrankfurterClient extends FrankfurterClient {

        private final Map<String, String> response;
        private String lastPath;

        private StubFrankfurterClient(Map<String, String> response) {
            super(WebClient.builder().build());
            this.response = response;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> T get(String path, ParameterizedTypeReference<T> responseType) {
            lastPath = path;
            if (path.equals("/currencies")) {
                return (T) response;
            }
            throw new IllegalArgumentException("Unexpected path: " + path);
        }

        String lastPath() {
            return lastPath;
        }
    }
}
