package co.id.allobank.finance.config.mapper;

import co.id.allobank.finance.model.response.SupportedCurrencyResponse;
import co.id.allobank.finance.model.response.SupportedCurrencyResponseBuilder;

import java.util.List;
import java.util.Map;

public class SupportedCurrenciesMapper {

    public static List<SupportedCurrencyResponse> map(Map<String,String> raw) {
        return raw.entrySet()
                .stream()
                .map(e -> SupportedCurrencyResponseBuilder.builder()
                                .code(e.getKey())
                                .name(e.getValue())
                                .build()
                )
                .toList();
    }
}
