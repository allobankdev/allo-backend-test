package co.id.allobank.finance.config.mapper;

import co.id.allobank.finance.model.response.HistoricalIDRUsdRateResponse;
import co.id.allobank.finance.model.response.HistoricalIDRUsdRateResponseBuilder;
import co.id.allobank.finance.model.response.HistoricalRatesRawResponse;

import java.util.List;

public class HistoricalRatesMapper {

    public static List<HistoricalIDRUsdRateResponse> map(HistoricalRatesRawResponse raw) {
        return raw.rates()
                .entrySet()
                .stream()
                .map(e -> HistoricalIDRUsdRateResponseBuilder.builder()
                        .date(e.getKey())
                        .rate(e.getValue().get("USD"))
                        .build()
                )
                .toList();
    }
}
