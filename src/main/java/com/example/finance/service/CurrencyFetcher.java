package com.example.finance.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class CurrencyFetcher implements IDRDataFetcher {

	private static final Logger log = LoggerFactory.getLogger(CurrencyFetcher.class);

	private final HitExternalApiService apiService;

	public CurrencyFetcher(HitExternalApiService apiService) {
		this.apiService = apiService;
	}

	@Override
	public String getType() {
		return "supported_currencies";
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object fetchData() {

		log.info("Start fetching supported currencies");

		try {
			Map<String, String> response = apiService.get(apiService.endpoints().getCurrencies(), null, Map.class);

			if (response == null || response.isEmpty()) {
				log.error("Currencies response is empty");
				throw new RuntimeException("Currencies response is empty");
			}

			log.info("Finish fetching supported currencies, Response data: {}", response);

			return response;

		} catch (Exception e) {
			log.error("Failed to fetch currencies", e);
			throw new RuntimeException("Failed to fetch currencies", e);
		}
	}
}