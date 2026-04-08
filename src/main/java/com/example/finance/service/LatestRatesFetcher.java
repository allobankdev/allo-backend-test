package com.example.finance.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.finance.util.SpreadCalculator;

@Component
public class LatestRatesFetcher implements IDRDataFetcher {

	private static final Logger log = LoggerFactory.getLogger(LatestRatesFetcher.class);

	private final HitExternalApiService apiService;

	private static final String Username = "nisaulchaira14";

	public LatestRatesFetcher(HitExternalApiService apiService) {
		this.apiService = apiService;
	}

	@Override
	public String getType() {
		return "latest_idr_rates";
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object fetchData() {
		log.info("Start fetching Latest IDR Rates");

		try {
			Map<String, Object> response = apiService.get(apiService.endpoints().getLatest(), "?base=IDR", Map.class);

			Map<String, Double> rates = (Map<String, Double>) response.get("rates");

			double usdRate = rates.get("USD");

			double spread = SpreadCalculator.calculateUsdBuySpread(usdRate, Username);

			response.put("USD_BuySpread_IDR", spread);
			log.info("Finish fetching Latest IDR Rates, Response data: {}", response);

			return response;
		} catch (Exception e) {
			log.error("Failed to fetch Latest IDR Rates", e);
			throw new RuntimeException("Failed to fetch Latest IDR Rates", e);
		}
	}
}