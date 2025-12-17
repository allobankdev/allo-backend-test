package com.example.allobank.service;

import com.example.allobank.dto.FinanceDataItemDto;
import com.example.allobank.dto.HistoricalRatesDto;
import com.example.allobank.exception.ExternalServiceException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component("historical_idr_usd")
@RequiredArgsConstructor
public class HistoricalRatesFetcher implements IDRDataFetcher {

    private final WebClient webClient;

    @Override
    public String resourceType() {
        return "historical_idr_usd";
    }

    @Override
    public List<FinanceDataItemDto> fetch() {
        // Must use the exact range from the prompt
        String path = "/2024-01-01..2024-01-05";

        HistoricalRatesDto dto = webClient.get()
                .uri(uriBuilder -> uriBuilder.path(path)
                        .queryParam("from", "IDR")
                        .queryParam("to", "USD")
                        .build())
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        resp -> resp.bodyToMono(String.class)
                                .defaultIfEmpty("")
                                .flatMap(body -> Mono.error(new ExternalServiceException(
                                        "Frankfurter timeseries returned " + resp.statusCode() + " body=" + body))))
                .bodyToMono(HistoricalRatesDto.class)
                .onErrorMap(ex -> (ex instanceof ExternalServiceException) ? ex :
                        new ExternalServiceException("Failed calling Frankfurter timeseries", ex))
                .block();

        if (dto == null || dto.getRates() == null) {
            throw new ExternalServiceException("Frankfurter timeseries response is empty");
        }

        List<FinanceDataItemDto> items = new ArrayList<>();

        for (Map.Entry<String, Map<String, BigDecimal>> entry : dto.getRates().entrySet()) {
            String date = entry.getKey();
            BigDecimal usdRate = entry.getValue() != null ? entry.getValue().get("USD") : null;

            if (usdRate == null) {
                // Strict: if a date missing USD, treat as external data issue
                throw new ExternalServiceException("Timeseries missing USD for date=" + date);
            }

            Map<String, Object> meta = new LinkedHashMap<>();
            meta.put("base", dto.getBase());
            meta.put("start_date", dto.getStart_date());
            meta.put("end_date", dto.getEnd_date());

            items.add(FinanceDataItemDto.builder()
                    .resourceType(resourceType())
                    .key(date)
                    .value(usdRate)
                    .meta(meta)
                    .build());
        }

        return List.copyOf(items);
    }
}