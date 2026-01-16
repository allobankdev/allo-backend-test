package com.allo.aggregator.runner;

import com.allo.aggregator.store.ExchangeRateStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.mockito.Mockito;
import org.springframework.core.ParameterizedTypeReference;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;

@SpringBootTest
class DataIngestionRunnerIntegrationTest {

  // We can't easily mock the WebClient created by FactoryBean with @MockBean
  // because the FactoryBean creates it.
  // However, we can mock the behavior of the WebClient if we intercept the
  // FactoryBean or the Bean definition.
  // A simpler approach for this constraints exercise:
  // Let's rely on the fact that if the context loads, the runner worked.
  // But we need to verify data in the store.
  // If we let real calls happen, we are testing the external API (flaky).
  // Better: Define a TestConfig that overrides the WebClient bean.

  @Configuration
  @Import(com.allo.aggregator.AggregatorApplication.class) // Import main config
  static class TestConfig {
    @Bean
    @Primary
    public WebClient webClient() {
      WebClient mockClient = mock(WebClient.class);
      WebClient.RequestHeadersUriSpec<?> uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
      WebClient.RequestHeadersSpec<?> headersSpec = mock(WebClient.RequestHeadersSpec.class);
      WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

      Mockito.doReturn(uriSpec).when(mockClient).get();
      Mockito.doReturn(headersSpec).when(uriSpec).uri(anyString());
      Mockito.doReturn(responseSpec).when(headersSpec).retrieve();

      Map<String, Object> mockData = new HashMap<>();
      mockData.put("mocked", "true");
      mockData.put("rates", new HashMap<>()); // Prevent NPE in strategy

      Mockito.doReturn(Mono.just(mockData)).when(responseSpec).bodyToMono(Map.class);
      Mockito.doReturn(Mono.just(mockData)).when(responseSpec)
          .bodyToMono(Mockito.<ParameterizedTypeReference<?>>any());

      return mockClient;
    }
  }

  @Autowired
  private ExchangeRateStore store;

  @Test
  void run_shouldHavePopulatedStore() {
    // Assert that the store contains the expected keys.
    // Since we mocked the WebClient to return data, the fetchers should have
    // succeeded.

    assertTrue(store.getAllData().containsKey("latest_idr_rates"), "Store should contain latest_idr_rates");
    assertTrue(store.getAllData().containsKey("historical_idr_usd"), "Store should contain historical_idr_usd");
    assertTrue(store.getAllData().containsKey("supported_currencies"), "Store should contain supported_currencies");

    @SuppressWarnings("unchecked")
    Map<String, Object> latest = (Map<String, Object>) store.getData("latest_idr_rates");
    assertNotNull(latest);
  }
}
