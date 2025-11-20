package com.allobank.assignment.client;

import com.allobank.assignment.config.CurrencyApiProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CurrencyApiClient {

	private final RestTemplate restTemplate;
	private final CurrencyApiProperties properties;

	public CurrencyApiClient(CurrencyApiProperties properties) {
		this.restTemplate = new RestTemplate();
		this.restTemplate.setErrorHandler(new FrankfurterErrorHandler());
		this.properties = properties;
	}

	public <T> T get(String endpoint, Class<T> clazz) {
		String url = properties.getBaseUrl() + endpoint;
		return restTemplate.getForObject(url, clazz);
	}
}
