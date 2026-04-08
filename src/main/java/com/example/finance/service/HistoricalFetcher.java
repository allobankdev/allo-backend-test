package com.example.finance.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class HistoricalFetcher implements IDRDataFetcher {

	private static final Logger log = LoggerFactory.getLogger(HistoricalFetcher.class);

	private final HitExternalApiService apiService;

	public HistoricalFetcher(HitExternalApiService apiService) {
		this.apiService = apiService;
	}

	@Override
	public String getType() {
		return "historical_idr_usd";
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object fetchData() {

		log.info("Start fetching historical");

		try {
			Map<String, String> response = apiService.get(apiService.endpoints().getHistorical(), "?from=IDR&to=USD",
					Map.class);

			if (response == null || response.isEmpty()) {
				log.error("Historical response is empty");
				throw new RuntimeException("Historical response is empty");
			}

			log.info("Finish fetching Historical, Response data: {}", response);

			return response;

		} catch (Exception e) {
			log.error("Failed to fetch Historical", e);
			throw new RuntimeException("Failed to fetch Historical", e);
		}
	}
}