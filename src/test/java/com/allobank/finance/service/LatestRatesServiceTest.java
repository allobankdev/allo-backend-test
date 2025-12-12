package com.allobank.finance.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import com.allobank.finance.client.FrankfurterClient;
import com.allobank.finance.dto.LatestRatesResponse;

import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
public class LatestRatesServiceTest {

	@Mock
	private FrankfurterClient frankfurterClient;

	@Mock
	private WebClient webClient;

	@Mock
	private WebClient.RequestHeadersUriSpec<?> uriSpec;

	@Mock
	private WebClient.RequestHeadersSpec<?> headersSpec;

	@Mock
	private WebClient.ResponseSpec responseSpec;

	private LatestRatesService latestRatesService;

	@Test
	public void fetchLatestRates_appliesSpreadFactor() {
		doReturn(webClient).when(frankfurterClient).getWebClient();
		doReturn(uriSpec).when(webClient).get();
		doReturn(headersSpec).when(uriSpec).uri("/latest?base=IDR");
		doReturn(responseSpec).when(headersSpec).retrieve();

		LatestRatesResponse apiResponse = new LatestRatesResponse();
		apiResponse.setBase("IDR");
		apiResponse.setRates(Map.of("USD", 0.000062));

		doReturn(Mono.just(apiResponse)).when(responseSpec).bodyToMono(LatestRatesResponse.class);

			// construct service with injected username
			String username = "palitojeremy";
			latestRatesService = new LatestRatesService(frankfurterClient, username);

			LatestRatesResponse resp = latestRatesService.fetchLatestRates();

			assertNotNull(resp);
			assertNotNull(resp.getUsdBuySpreadIdr());

			double usdRate = apiResponse.getRates().get("USD");
			long sum = username.toLowerCase().chars().sum();
			double spreadFactor = (sum % 1000) / 100000.0;
			double expected = (1.0 / usdRate) * (1.0 + spreadFactor);

			assertEquals(expected, resp.getUsdBuySpreadIdr(), 1e-6);
	}
}
