package com.example.allobank.project.strategy;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.allobank.project.dto.LatestIdrRates;
import com.example.allobank.project.utils.ConstantEnum;

import lombok.RequiredArgsConstructor;

@Service("latest_idr_rates")
@RequiredArgsConstructor
public class LatestIdrRatesFetcher implements IDRDataFetcher {

	@Autowired
    RestTemplate restTemplate;

	@Value("${frankfurter.api.base-url}")
    private String baseUrl;
	
    @Value("${personal.github-username}")
    private String githubUsername;

    @Value("${personal.resource-types.latest}")
    private String resourceType;
    
    @Override
	public String getResourceType() {
		return resourceType;
	}
    
    @Override
    public Object fetchData(String... params) {
    	String url = baseUrl+"/latest?base=IDR";
        Map<String, Object> apiResponse = restTemplate.getForObject(url, Map.class);

        if (apiResponse == null || apiResponse.isEmpty()) {
            throw new IllegalStateException("No response from Frankfurter");
        }
        
        Map<String, Double> rates = (Map<String, Double>) apiResponse.get(ConstantEnum.RATES);
        double rateUsd = rates.get("USD");

        int sumAscii = githubUsername.toLowerCase().chars().sum();
        double spreadFactor = (sumAscii % 1000) / 100_000.0;
        double usdBuySpread = (1 / rateUsd) * (1 + spreadFactor);

        return new LatestIdrRates(
                (String) apiResponse.get(ConstantEnum.BASE),
                (String) apiResponse.get(ConstantEnum.DATE),
                rates,
                usdBuySpread
        );
    }

	
}
