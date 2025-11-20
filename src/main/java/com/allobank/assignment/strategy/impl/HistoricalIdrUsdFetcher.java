package com.allobank.assignment.strategy.impl;

import com.allobank.assignment.config.CurrencyApiProperties;
import com.allobank.assignment.strategy.CurrencyDataFetcher;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Component("historical_idr_usd")
public class HistoricalIdrUsdFetcher implements CurrencyDataFetcher {

	private final RestTemplate restTemplate;
	private final CurrencyApiProperties props;

	public HistoricalIdrUsdFetcher(RestTemplate restTemplate, CurrencyApiProperties props) {
		this.restTemplate = restTemplate;
		this.props = props;
	}

	@Override
	public CompletableFuture<Object> fetch(String queryParam) {

		return CompletableFuture.supplyAsync(() -> {

			String url = props.getBaseUrl() + props.getHistoricalEndpoint();

			Object json = restTemplate.getForObject(url, Object.class);

			return json;
		});
	}

	@Override
	public String getName() {
		return "historical_idr_usd";
	}
}


