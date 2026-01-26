package com.example.AlloBank.strategy;

import com.example.AlloBank.dto.CurrencyDto;
import com.example.AlloBank.response.CurrenciesResponse;
import com.example.AlloBank.store.FinanceStore;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SupportedCurrenciesStrategy implements FinanceDataStrategy<CurrencyDto>{

    private final FinanceStore financeStore;

    public SupportedCurrenciesStrategy(FinanceStore financeStore) {
        this.financeStore = financeStore;
    }

    @Override
    public String getType() {
        return "currencies";
    }

    @Override
    public List<CurrencyDto> getData() {
        CurrenciesResponse data = financeStore.getCurrencies();
        Map<String, String> currencies = data.getCurrencies();

        return currencies.entrySet().stream()
                .map(entry -> toDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private CurrencyDto toDto(String code, String name) {
        CurrencyDto dto = new CurrencyDto();
        dto.setCode(code);
        dto.setName(name);
        return dto;
    }

}
