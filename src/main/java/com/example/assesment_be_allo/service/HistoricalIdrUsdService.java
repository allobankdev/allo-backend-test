package com.example.assesment_be_allo.service;
import com.example.assesment_be_allo.dto.HistoricalRateResponse;
import com.example.assesment_be_allo.repository.ExternalApiRepository;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class HistoricalIdrUsdService {

    private final ExternalApiRepository repository;

    public HistoricalIdrUsdService(ExternalApiRepository repository) {
        this.repository = repository;
    }

    public Object fetchHistoricalRates() {
        Map<String, Object> apiResponse = repository.fetchHistoricalRates(
                "2024-01-01", "2024-01-05", "IDR", "USD");

        if (apiResponse == null) {
            return Collections.emptyList();
        }

        List<HistoricalRateResponse> results = new ArrayList<>();

        // Extract historical rates
        Map<String, Object> rates = (Map<String, Object>) apiResponse.get("rates");
        if (rates != null) {
            for (Map.Entry<String, Object> entry : rates.entrySet()) {
                HistoricalRateResponse response = new HistoricalRateResponse();
                response.setDate(entry.getKey());
                response.setBase((String) apiResponse.get("base"));
                response.setRates((Map<String, Object>) entry.getValue());
                results.add(response);
            }
        }

        return results;
    }
}
