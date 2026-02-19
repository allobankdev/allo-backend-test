package co.id.allobank.finance.config.strategy;

import co.id.allobank.finance.config.mapper.LatestRatesMapper;
import co.id.allobank.finance.model.response.LatestRatesRawResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component("latest_idr_rates")
@RequiredArgsConstructor
public class LatestIDRRatesFetcher implements IDRDataFetcher {

    private final WebClient webClient;

    @Value("${frankfurter.github-username}")
    private String username;

    @Override
    public String getResourceType() {
        return "latest_idr_rates";
    }

    @Override
    public Object fetchData() {
        LatestRatesRawResponse raw = webClient.get()
                .uri("/latest?base=IDR")
                .retrieve()
                .bodyToMono(LatestRatesRawResponse.class)
                .block();

        return LatestRatesMapper.map(raw, username);
    }
}
