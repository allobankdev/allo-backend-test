package com.example.allobank.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.allobank.calculator.SpreadCalculator;
import com.example.allobank.calculator.SpreadFactorCalculator;
import com.example.allobank.dto.RatesResponseDTO;
import com.example.allobank.dto.SpreadDetailDTO;

@Component
public class LatestIdrRatesFetcher implements IDRDataFetcher {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SpreadCalculator spreadCalculator;

    @Autowired
    private SpreadFactorCalculator spreadFactorCalculator;

    @Autowired
    private org.springframework.core.env.Environment env;

    @Override
    public String getResourceType() {
        return "latest_idr_rates";
    }

    @Override
    public Object fetch() {

        String url = "https://api.frankfurter.app/latest?base=IDR";

        Map<String, Object> apiResponse =
                restTemplate.getForObject(url, Map.class);

        Map<String, Object> rawRates =
                (Map<String, Object>) apiResponse.get("rates");

        RatesResponseDTO dto = new RatesResponseDTO();
        dto.setAmount(new BigDecimal(apiResponse.get("amount").toString()));
        dto.setBase(apiResponse.get("base").toString());
        dto.setDate(apiResponse.get("date").toString());

        Map<String, BigDecimal> rates = new HashMap<>();
        Map<String, SpreadDetailDTO> spreadMap = new HashMap<>();

        String githubUsername =
                env.getProperty("app.github.username");

        BigDecimal spreadFactor =
                spreadFactorCalculator.calculateFromUsername(githubUsername);

        for (Map.Entry<String, Object> entry : rawRates.entrySet()) {

            String currency = entry.getKey();
            BigDecimal rate =
                    new BigDecimal(entry.getValue().toString());

            rates.put(currency, rate);

            SpreadDetailDTO spread =
                    spreadCalculator.calculate(rate, spreadFactor);

            spreadMap.put(currency, spread);
        }

        dto.setRates(rates);
        dto.setSpread(spreadMap);


        BigDecimal usdRate = rates.get("USD");
        if (usdRate != null) {
            BigDecimal usdBuySpread =
                    BigDecimal.ONE
                            .divide(usdRate, 10, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.ONE.add(spreadFactor))
                            .setScale(2, RoundingMode.HALF_UP);

            dto.setUsdBuySpreadIdr(usdBuySpread);
        }

        return dto;
    }
}
