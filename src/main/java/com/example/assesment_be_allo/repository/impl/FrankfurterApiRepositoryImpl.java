package com.example.assesment_be_allo.repository.impl;
import com.example.assesment_be_allo.exception.ExternalApiException;
import com.example.assesment_be_allo.repository.ExternalApiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Repository
public class FrankfurterApiRepositoryImpl implements ExternalApiRepository {

    private static final String BASE_URL = "https://api.frankfurter.app";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Map<String, Object> fetchLatestRates(String base) {
        try {
            String url = BASE_URL + "/latest?base=" + base;
            return restTemplate.getForObject(url, Map.class);
        } catch (Exception e) {
            throw new ExternalApiException("Failed to fetch latest rates from external API", e);
        }
    }

    @Override
    public Map<String, Object> fetchHistoricalRates(String startDate, String endDate,
                                                    String from, String to) {
        try {
            String url = String.format("%s/%s..%s?from=%s&to=%s",
                    BASE_URL, startDate, endDate, from, to);
            return restTemplate.getForObject(url, Map.class);
        } catch (Exception e) {
            throw new ExternalApiException("Failed to fetch historical rates from external API", e);
        }
    }

    @Override
    public Map<String, String> fetchSupportedCurrencies() {
        try {
            String url = BASE_URL + "/currencies";
            return restTemplate.getForObject(url, Map.class);
        } catch (Exception e) {
            throw new ExternalApiException("Failed to fetch supported currencies from external API", e);
        }
    }
}