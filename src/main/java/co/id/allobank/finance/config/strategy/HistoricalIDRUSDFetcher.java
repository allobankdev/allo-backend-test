package co.id.allobank.finance.config.strategy;

import co.id.allobank.finance.config.mapper.HistoricalRatesMapper;
import co.id.allobank.finance.model.response.HistoricalRatesRawResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component("historical_idr_usd")
@RequiredArgsConstructor
public class HistoricalIDRUSDFetcher implements IDRDataFetcher {

    private final WebClient webClient;

    @Override
    public String getResourceType() {
        return "historical_idr_usd";
    }

    @Override
    public Object fetchData() {
        HistoricalRatesRawResponse raw = webClient.get()
                .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")
                .retrieve()
                .bodyToMono(HistoricalRatesRawResponse.class)
                .block();

        return HistoricalRatesMapper.map(raw);
    }
}
