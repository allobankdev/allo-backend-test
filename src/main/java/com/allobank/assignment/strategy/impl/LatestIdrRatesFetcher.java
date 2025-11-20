package com.allobank.assignment.strategy.impl;

import com.allobank.assignment.config.CurrencyApiProperties;
import com.allobank.assignment.strategy.CurrencyDataFetcher;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component("latest_idr_rates")
public class LatestIdrRatesFetcher implements CurrencyDataFetcher {

	private final RestTemplate restTemplate;
	private final CurrencyApiProperties props;

	public LatestIdrRatesFetcher(RestTemplate restTemplate, CurrencyApiProperties props) {
		this.restTemplate = restTemplate;
		this.props = props;
	}

	@Override
	public CompletableFuture<Object> fetch(String queryParam) {

		return CompletableFuture.supplyAsync(() -> {

			String url = props.getBaseUrl() + props.getLatestEndpoint();

			Map<String, Object> json = restTemplate.getForObject(url, Map.class);
			if (json == null)
				return null;

			Map<String, Number> rates = (Map<String, Number>) json.get("rates");
			Number usdRate = rates.get("USD");

			if (usdRate != null) {

				double rateUsd = usdRate.doubleValue();
				double spread = computeSpreadFactor(props.getGithubUsername());

				double usdBuySpreadIdr = (1 / rateUsd) * (1 + spread);

				json.put("spreadFactor", spread);
				json.put("USD_BuySpread_IDR", usdBuySpreadIdr);
			}

			return json;
		});
	}

	private double computeSpreadFactor(String username) {
		int sum = 0;
		for (char c : username.toLowerCase().toCharArray()) {
			sum += c;
		}
		return (sum % 1000) / 100000.0;
	}

	@Override
	public String getName() {
		return "latest_idr_rates";
	}
}



