package com.api.allorestapi.strategy;

import com.api.allorestapi.model.FinanceDataResponse;
import com.api.allorestapi.model.ResourceType;
import com.api.allorestapi.service.SpreadCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
// import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class LatestIdrRatesFetcher implements IDRDataFetch {

    private final WebClient frankfurterWebClient;
    private final SpreadCalculator spreadCalculator;

    @Override
    public String getResourceType() {
        return ResourceType.LATEST_IDR_RATES.getValue();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Mono<FinanceDataResponse> fetch() {
        log.debug("Strategy fetch: latest_idr_rates");
        return frankfurterWebClient
                .get()
                .uri(uri -> uri.path("/latest").queryParam("base", "IDR").build())
                .retrieve()
                .bodyToMono(Map.class)
                .map(raw -> {
                    String base = (String) raw.get("base");
                    String date = (String) raw.get("date");
                    Map<String, Number> rates = (Map<String, Number>) raw.get("rates");

                    BigDecimal rateUsd = new BigDecimal(rates.get("USD").toString());
                    BigDecimal usdBuySpread = spreadCalculator.calculate(rateUsd);

                    Map<String, Object> entry = new LinkedHashMap<>();
                    entry.put("base", base);
                    entry.put("date", date);
                    entry.put("rates", rates);
                    entry.put("USD_BuySpread_IDR", usdBuySpread);
                    entry.put("spreadFactor", spreadCalculator.getSpreadFactor());

                    return FinanceDataResponse.builder()
                            .resourceType(getResourceType())
                            .data(List.of(entry))
                            .build();
                });
    }
}
