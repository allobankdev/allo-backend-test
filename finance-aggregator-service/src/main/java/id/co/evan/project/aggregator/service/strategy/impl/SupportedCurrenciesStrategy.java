package id.co.evan.project.aggregator.service.strategy.impl;

import id.co.evan.project.aggregator.model.response.UnifiedFinanceResponse;
import id.co.evan.project.aggregator.service.strategy.IDRDataFetcher;
import id.co.evan.project.aggregator.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service(Constants.SUPPORTED_CURRENCIES)
@RequiredArgsConstructor
public class SupportedCurrenciesStrategy implements IDRDataFetcher {
    private final WebClient webClient;

    @Override
    public Mono<UnifiedFinanceResponse> fetchData() {
        var now = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSXXX"));

        return webClient.get()
            .uri(Constants.CURRENCIES)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
            .map(res -> {
                var list = res.entrySet().stream()
                    .map(e -> {
                        Map<String, Object> m = new HashMap<>();
                        m.put("code", e.getKey());
                        m.put("name", e.getValue());
                        return m;
                    }).toList();
                return new UnifiedFinanceResponse(getResourceType(), now, list);
            });
    }

    @Override
    public String getResourceType() {
        return Constants.SUPPORTED_CURRENCIES;
    }
}