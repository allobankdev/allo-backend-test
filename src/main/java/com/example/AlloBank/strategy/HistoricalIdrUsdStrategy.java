package com.example.AlloBank.strategy;

import com.example.AlloBank.dto.HistoricalRateDto;
import com.example.AlloBank.response.HistoricalRatesResponse;
import com.example.AlloBank.store.FinanceStore;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class HistoricalIdrUsdStrategy implements FinanceDataStrategy<HistoricalRateDto>{

    private final FinanceStore financeStore;

    public HistoricalIdrUsdStrategy(FinanceStore financeStore) {
        this.financeStore = financeStore;
    }

    @Override
    public String getType() {
        return "historical_idr_usd";
    }

    @Override
    public List<HistoricalRateDto> getData() {
        HistoricalRatesResponse data = financeStore.getHistoricalRates();

        Map<String, Map<String, Double>> rates = data.getRates();

        return rates.entrySet().stream()
                .map(entry -> toDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private HistoricalRateDto toDto(String date, Map<String, Double> currencyRates) {
        HistoricalRateDto dto = new HistoricalRateDto();
        dto.setDate(date);
        Double usdRate = currencyRates.get("USD");
        dto.setRate(usdRate != null ? usdRate : 0.0);

        return dto;
    }

}
