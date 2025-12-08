package com.example.allobank.project.strategy;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.allobank.project.dto.SupportedCurrencies;

import lombok.RequiredArgsConstructor;

@Service("supported_currencies")
@RequiredArgsConstructor
public class SupportedCurrenciesFetcher implements IDRDataFetcher {

	@Autowired
    RestTemplate restTemplate;

	@Value("${frankfurter.api.base-url}")
    private String baseUrl;

    @Value("${personal.resource-types.supported}")
    private String resourceType;
    
    @Override
	public String getResourceType() {
		return resourceType;
	}
    
    @Override
    public Object fetchData(String... params) {
        String url = baseUrl+"/currencies";
        Map<String, String> apiResponse = restTemplate.getForObject(url, Map.class);

        if (apiResponse == null || apiResponse.isEmpty()) {
            throw new IllegalStateException("Empty response from Frankfurter");
        }

        return new SupportedCurrencies(apiResponse);
    }

	
}
