package com.frankfurter.aggregator.strategy;

import com.frankfurter.aggregator.config.AppProperties;
import com.frankfurter.aggregator.dto.internal.FinanceDataResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class HistoricalIdrUsdStrategy implements IDRDataFetcher {
    private final RestTemplate restTemplate;
    private final AppProperties appProperties;

    public HistoricalIdrUsdStrategy(RestTemplate restTemplate, AppProperties appProperties) {
        this.restTemplate = restTemplate;
        this.appProperties = appProperties;
    }

    @Override
    public String getResourceType() {
        return "historical_idr_usd";
    }

    @Override
    public FinanceDataResponse fetchData() {
        try {
            String startDate = appProperties.getApi().getHistorical().getStartDate().toString();
            String endDate = appProperties.getApi().getHistorical().getEndDate().toString();
            String fromCurrency = appProperties.getApi().getHistorical().getFromCurrency();
            String toCurrency = appProperties.getApi().getHistorical().getToCurrency();
            
            String url = "https://api.frankfurter.app/" + 
                        String.format("%s..%s?from=%s&to=%s", 
                        startDate, endDate, fromCurrency, toCurrency);
            
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            
            Map<String, Object> data = new HashMap<>();
            
            if (response != null && response.get("rates") instanceof Map) {
                Map<String, Map<String, Double>> ratesMap = 
                    (Map<String, Map<String, Double>>) response.get("rates");
                
                // TRANSFORM MAP TO ARRAY as required
                List<Map<String, Object>> ratesArray = new ArrayList<>();
                ratesMap.forEach((date, dailyRates) -> {
                    Map<String, Object> dayData = new HashMap<>();
                    dayData.put("date", date);
                    dayData.put("rates", dailyRates);
                    ratesArray.add(dayData);
                });
                
                data.put("rates", ratesArray);  // ← ARRAY not Map
                data.put("base", response.get("base"));
            }
            
            data.put("start_date", startDate);
            data.put("end_date", endDate);
            data.put("from", fromCurrency);
            data.put("to", toCurrency);
            
            return new FinanceDataResponse(getResourceType(), LocalDateTime.now(), data);
            
        } catch (Exception e) {
            System.err.println("Error fetching historical rates: " + e.getMessage());
            return null;
        }
    }
}