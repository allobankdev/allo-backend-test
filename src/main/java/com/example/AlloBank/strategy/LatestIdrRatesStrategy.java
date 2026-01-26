package com.example.AlloBank.strategy;

import com.example.AlloBank.dto.LatestRateDto;
import com.example.AlloBank.response.LatestRatesResponse;
import com.example.AlloBank.store.FinanceStore;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class LatestIdrRatesStrategy implements FinanceDataStrategy<LatestRateDto> {
    private final FinanceStore financeStore;
    private final double spreadFactor;

    public LatestIdrRatesStrategy(
            FinanceStore financeStore,
            @Value("${github.username}") String githubUsername
    ) {
        this.financeStore = financeStore;
        this.spreadFactor = calculateSpreadFactor(githubUsername);
    }

    @Override
    public String getType() {
        return "latest_idr_rates";
    }

    @Override
    public List<LatestRateDto> getData() {
        LatestRatesResponse data = financeStore.getLatestRates();
        Map<String, Double> rates = data.getRates();

        return rates.entrySet().stream()
                .map(entry -> toDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private LatestRateDto toDto(String currency, Double rate) {
        LatestRateDto dto = new LatestRateDto();
        dto.setCurrency(currency);
        dto.setRate(rate);

        if ("USD".equals(currency)) {
            double usdBuySpreadIdr = (1 / rate) * (1 + spreadFactor);
            dto.setUsdBuySpreadIdr(usdBuySpreadIdr);
        } else {
            dto.setUsdBuySpreadIdr(0.0);
        }

        return dto;
    }

    private double calculateSpreadFactor(String username) {
        String lower = username.toLowerCase();
        int sum = 0;
        for (char c : lower.toCharArray()) {
            sum += (int) c;
        }
        return (sum % 1000) / 100000.0;
    }
}
