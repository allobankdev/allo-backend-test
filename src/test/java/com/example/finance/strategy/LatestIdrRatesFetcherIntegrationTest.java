package com.example.finance.strategy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * A lightweight integration test that boots the Spring context and verifies
 * that the {@code app.github.username} property is injected into the
 * {@link LatestIdrRatesFetcher} bean. {@link RestTemplate} is mocked so that
 * the test doesn’t hit the real Frankfurter service and then assert that the 
 * spread value returned by the fetcher matches the value computed from the 
 * configured username.
 */
@SpringBootTest(properties = "app.github.username=testuser")
class LatestIdrRatesFetcherIntegrationTest {

    @Autowired
    private LatestIdrRatesFetcher fetcher;

    @Autowired
    private RestTemplate restTemplate;

    @TestConfiguration
    static class MockRestTemplateConfig {
        @Bean
        @Primary
        public RestTemplate restTemplate() {
            RestTemplate rt = org.mockito.Mockito.mock(RestTemplate.class);
            when(rt.getForObject(org.mockito.ArgumentMatchers.contains("latest"), org.mockito.ArgumentMatchers.eq(Map.class)))
                    .thenReturn(Map.of("rates", Map.of("USD", 15000.0)));
            return rt;
        }
    }

    @Test
    void githubUsernameValue_isUsedWhenCalculatingSpread() {
        // arrange – stub the external call
        when(restTemplate.getForObject(contains("latest"), eq(Map.class)))
                .thenReturn(Map.of("rates", Map.of("USD", 15000.0)));

        // act
        List<Map<String, Object>> data = fetcher.fetchData();

        // compute expected spread based on the same algorithm used in the
        // production class.
        String username = "testuser";
        int sum = 0;
        for (char c : username.toLowerCase().toCharArray()) {
            sum += c;
        }
        double expectedSpread = (sum % 1000) / 100000.0;

        Map<String, Object> usdEntry = data.stream()
                .filter(m -> "USD".equals(m.get("currency")))
                .findFirst()
                .orElseThrow();

        assertThat(usdEntry.get("USD_BuySpread_IDR")).isEqualTo(expectedSpread);
    }
}
