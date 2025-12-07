package id.tisnanda.allobank.allo_bank_backend_test.strategy.impl;

import id.tisnanda.allobank.allo_bank_backend_test.dto.strategy.HistoricalIDRUSDResponseDTO;
import id.tisnanda.allobank.allo_bank_backend_test.exception.BadRequestException;
import id.tisnanda.allobank.allo_bank_backend_test.strategy.IDRDataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("historical_idr_usd")
public class HistoricalIDRUSDFetcher implements IDRDataFetcher<HistoricalIDRUSDResponseDTO> {

    @Value("${external.frankfurter.historical-url}")
    public String historicalUrl;

    @Autowired
    public RestTemplate restTemplate;

    @Override
    public List<HistoricalIDRUSDResponseDTO> fetchData() {

        if (restTemplate == null) {
            throw new BadRequestException("RestTemplate must be set before fetching data");
        }

        Map<String, Object> response = restTemplate.getForObject(historicalUrl, Map.class);

        if (response == null || !response.containsKey("rates")) {
            throw new BadRequestException("Failed to fetch historical IDR->USD rates");
        }

        Map<String, Map<String, Object>> rates = (Map<String, Map<String, Object>>) response.get("rates");
        List<HistoricalIDRUSDResponseDTO> result = new ArrayList<>();

        for (Map.Entry<String, Map<String, Object>> entry : rates.entrySet()) {
            String date = entry.getKey();
            Map<String, Object> usdMap = entry.getValue();
            Double usdValue = ((Number) usdMap.get("USD")).doubleValue();

            result.add(new HistoricalIDRUSDResponseDTO(date, usdValue));
        }

        return result;
    }

}
