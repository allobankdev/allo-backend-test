package com.example.allobank.project.strategy;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.allobank.project.dto.HistoricalIdrUsd;
import com.example.allobank.project.utils.ConstantEnum;

import lombok.RequiredArgsConstructor;

@Service("historical_idr_usd")
@RequiredArgsConstructor
public class HistoricalIdrFetcher implements IDRDataFetcher {

	@Autowired
    RestTemplate restTemplate;

	@Value("${frankfurter.api.base-url}")
    private String baseUrl;

    @Value("${personal.resource-types.historical}")
    private String resourceType;
    
    @Value("${personal.resource-types.historical-start}")
    private String startDate;
    
    @Value("${personal.resource-types.historical-end}")
    private String endDate;
    
    @Override
	public String getResourceType() {
		return resourceType;
	}
    
    @Override
    public Object fetchData(String... params) {
    	String url =  baseUrl + "/2024-01-01..2024-01-05?from=IDR&to=USD";
    	
        Map<String, Object> apiResponse = restTemplate.getForObject(url, Map.class);

        if (apiResponse == null || apiResponse.isEmpty()) {
            throw new IllegalStateException("Empty response from Frankfurter");
        }
        
        Map<String, Double> rates = (Map<String, Double>) apiResponse.get(ConstantEnum.RATES);

        return new HistoricalIdrUsd(startDate,endDate,rates);
    }

	
}
