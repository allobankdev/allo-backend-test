package co.id.allobank.finance.config.mapper;

import co.id.allobank.finance.model.response.LatestIDRRateResponse;
import co.id.allobank.finance.model.response.LatestIDRRateResponseBuilder;
import co.id.allobank.finance.model.response.LatestRatesRawResponse;
import co.id.allobank.finance.utils.SpreadCalculator;

import java.util.List;

public class LatestRatesMapper {

    public static List<LatestIDRRateResponse> map(LatestRatesRawResponse raw, String username) {
        double usdRate = raw.rates().get("USD");

        return raw.rates()
                .entrySet()
                .stream()
                .map(e -> LatestIDRRateResponseBuilder.builder()
                        .currency(e.getKey())
                        .rate(e.getValue())
                        .usdBuySpreadIdr(SpreadCalculator.calculateUSDBuySpread(usdRate, username))
                        .build()
                )
                .toList();
    }
}
