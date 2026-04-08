package com.example.finance.service;

import com.example.finance.config.FrankfurterProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HitExternalApiService {

	private static final Logger log = LoggerFactory.getLogger(HitExternalApiService.class);

	private final RestTemplate restTemplate;
	private final FrankfurterProperties properties;

	public HitExternalApiService(@Qualifier("frankfurterRestTemplate") RestTemplate restTemplate,
			FrankfurterProperties properties) {
		this.restTemplate = restTemplate;
		this.properties = properties;
	}

	private HttpEntity<String> createEntity() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("User-Agent", "Mozilla/5.0");
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new HttpEntity<>(headers);
	}

	public <T> T get(String endpoint, String query, Class<T> clazz) {

		String url = properties.getBaseUrl() + endpoint;

		try {

			if (query != null) {
				url += query;
			}

			ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.GET, createEntity(), clazz);

			if (!response.getStatusCode().is2xxSuccessful()) {
				throw new RuntimeException("External API error: " + response.getStatusCode());
			}

			return response.getBody();

		} catch (Exception e) {
			log.error("Failed calling API: {}", url, e);
			throw new RuntimeException("Failed calling API", e);
		}
	}

	public FrankfurterProperties.Endpoints endpoints() {
		return properties.getEndpoints();
	}
}
