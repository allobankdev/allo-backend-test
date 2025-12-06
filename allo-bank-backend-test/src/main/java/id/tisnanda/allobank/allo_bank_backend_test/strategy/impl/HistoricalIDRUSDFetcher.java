package id.tisnanda.allobank.allo_bank_backend_test.strategy.impl;

import id.tisnanda.allobank.allo_bank_backend_test.exception.BadRequestException;
import id.tisnanda.allobank.allo_bank_backend_test.strategy.IDRDataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("historical_idr_usd")
public class HistoricalIDRUSDFetcher implements IDRDataFetcher {

    @Value("${external.frankfurter.historical-url}")
    public String historicalUrl;

    @Autowired
    public RestTemplate restTemplate;

    @Override
    public List<Map<String, Object>> fetchData() {
        if (restTemplate == null) {
            throw new BadRequestException("RestTemplate must be set before fetching data");
        }

        Map<String, Object> response = restTemplate.getForObject(historicalUrl, Map.class);

        if (response == null || !response.containsKey("rates")) {
            throw new BadRequestException("Failed to fetch historical IDR->USD rates");
        }

        Map<String, Object> rates = (Map<String, Object>) response.get("rates");

        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, Object> entry : rates.entrySet()) {
            Map<String, Object> record = new HashMap<>();
            record.put("date", entry.getKey());
            Map<String, Object> usdMap = (Map<String, Object>) entry.getValue();
            record.put("USD", usdMap.get("USD"));
            result.add(record);
        }

        return result;
    }

}
