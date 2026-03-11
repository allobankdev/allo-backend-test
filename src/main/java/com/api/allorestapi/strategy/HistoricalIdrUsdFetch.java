package com.api.allorestapi.strategy;

import com.api.allorestapi.model.FinanceDataResponse;
import com.api.allorestapi.model.ResourceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class HistoricalIdrUsdFetch implements IDRDataFetch {

    private final WebClient frankfurterWebClient;
    private final String startDate;
    private final String endDate;
    private final String fromCurrency;
    private final String toCurrency;

    public HistoricalIdrUsdFetch(
            WebClient frankfurterWebClient,
            @Value("${frankfurter.historical.start-date:2024-01-01}") String startDate,
            @Value("${frankfurter.historical.end-date:2024-01-05}") String endDate,
            @Value("${frankfurter.historical.from-currency:IDR}") String fromCurrency,
            @Value("${frankfurter.historical.to-currency:USD}") String toCurrency) {
        this.frankfurterWebClient = frankfurterWebClient;
        this.startDate = startDate;
        this.endDate = endDate;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
    }

    @Override
    public String getResourceType() {
        return ResourceType.HISTORICAL_IDR_USD.getValue();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Mono<FinanceDataResponse> fetch() {
        String path = String.format("/%s..%s", startDate, endDate);
        log.debug("Strategy fetch: historical_idr_usd — {}", path);

        return frankfurterWebClient
                .get()
                .uri(uri -> uri.path(path)
                        .queryParam("from", fromCurrency)
                        .queryParam("to", toCurrency)
                        .build())
                .retrieve()
                .bodyToMono(Map.class)
                .map(raw -> {
                    Map<String, Map<String, Number>> rates =
                            (Map<String, Map<String, Number>>) raw.get("rates");

                    List<Object> data = rates.entrySet().stream()
                            .sorted(Map.Entry.comparingByKey())
                            .map(e -> {
                                Map<String, Object> item = new LinkedHashMap<>();
                                item.put("date", e.getKey());
                                item.put("rates", e.getValue());
                                return (Object) item;
                            })
                            .collect(Collectors.toList());

                    return FinanceDataResponse.builder()
                            .resourceType(getResourceType())
                            .data(data)
                            .build();
                });
    }
}
