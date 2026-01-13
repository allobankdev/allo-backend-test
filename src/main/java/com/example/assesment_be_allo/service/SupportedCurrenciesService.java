package com.example.assesment_be_allo.service;
import com.example.assesment_be_allo.dto.CurrencyResponse;
import com.example.assesment_be_allo.repository.ExternalApiRepository;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class SupportedCurrenciesService {

    private final ExternalApiRepository repository;

    public SupportedCurrenciesService(ExternalApiRepository repository) {
        this.repository = repository;
    }

    public Object fetchSupportedCurrencies() {
        Map<String, String> apiResponse = repository.fetchSupportedCurrencies();

        if (apiResponse == null) {
            return Collections.emptyList();
        }

        List<CurrencyResponse> results = new ArrayList<>();
        for (Map.Entry<String, String> entry : apiResponse.entrySet()) {
            CurrencyResponse response = new CurrencyResponse(
                    entry.getKey(), entry.getValue());
            results.add(response);
        }

        return results;
    }
}