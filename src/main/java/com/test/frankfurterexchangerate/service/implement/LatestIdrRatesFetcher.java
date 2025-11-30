package com.test.frankfurterexchangerate.service.implement;

import com.test.frankfurterexchangerate.dto.FrankfurterLatestDto;
import com.test.frankfurterexchangerate.service.IDRDataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

//ToDo
@Component("latest_idr_rates")
public class LatestIdrRatesFetcher implements IDRDataFetcher {
    private final String baseUrl;
    private final String githubUsername;

    private final WebClient client;
    private final RestTemplate restTemplate;

    public LatestIdrRatesFetcher(
            @Value("${frankfurter.base-url}") String baseUrl,
            @Value("${github.username}") String githubUsername,
            WebClient client, RestTemplate restTemplate) {
        this.client = client;
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.githubUsername = githubUsername;
    }

    @Override
    public List<Object> fetchData() throws Exception {
        String url = baseUrl + "/latest?base=IDR";
        ResponseEntity<FrankfurterLatestDto> resp = restTemplate.getForEntity(url, FrankfurterLatestDto.class);
        FrankfurterLatestDto dto = resp.getBody();

        Double rateUsd = null;
        if(dto != null && dto.getRates() != null) {
            rateUsd = dto.getRates().get("USD");
        }

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("base", dto != null ? dto.getBase() : "IDR");
        out.put("date", dto != null ? dto.getDate() : null);
        out.put("rate", rateUsd);

        if(rateUsd != null && rateUsd != 0.0) {
            double spread = computeSpreadFactor(githubUsername);
            double usdBurSpreadIdr = (1.0 / rateUsd) * (1.0 + spread);

            out.put("USD_BuySpread_IDR", usdBurSpreadIdr);
            out.put("spread_factor", spread);
        } else {
            out.put("USD_BuySpread_IDR", null);
            out.put("spread_factor", null);
        }

        List<Object> result = new ArrayList<>();
        result.add(out);

        return result;
    }

    public double computeSpreadFactor(String username) {
        String lower = username == null ? "" : username.toLowerCase(Locale.ROOT);
        int sum = 0;
        for (char c : lower.toCharArray()) sum += (int) c;
        int mod = sum % 1000;
        return mod / 100000.0;
    }
}
