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


@Component("supported_currencies")
public class SupportedCurrenciesFetcher implements IDRDataFetcher {

    @Autowired
    public RestTemplate restTemplate;

    @Value("${external.frankfurter.currencies-url}")
    public String currenciesUrl;

    @Override
    public List<Map<String, Object>> fetchData() {
        if (restTemplate == null) {
            throw new BadRequestException("RestTemplate must be set before fetching data");
        }

        Map<String, String> response = restTemplate.getForObject(currenciesUrl, Map.class);

        if (response == null) {
            throw new BadRequestException("Failed to fetch supported currencies");
        }

        List<Map<String, Object>> result = new ArrayList<>();
        response.forEach((code, name) -> {
            Map<String, Object> record = new HashMap<>();
            record.put("code", code);
            record.put("name", name);
            result.add(record);
        });

        return result;
    }

}
