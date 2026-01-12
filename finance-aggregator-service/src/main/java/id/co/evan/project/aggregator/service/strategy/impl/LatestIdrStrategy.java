package id.co.evan.project.aggregator.service.strategy.impl;

import id.co.evan.project.aggregator.config.properties.FinanceProperties;
import id.co.evan.project.aggregator.config.properties.GithubProperties;
import id.co.evan.project.aggregator.fault.ResourceNotFoundException;
import id.co.evan.project.aggregator.model.response.ExternalRateResponse;
import id.co.evan.project.aggregator.model.response.UnifiedFinanceResponse;
import id.co.evan.project.aggregator.service.strategy.IDRDataFetcher;
import id.co.evan.project.aggregator.util.Constants;
import id.co.evan.project.aggregator.util.ErrorCode;
import id.co.evan.project.aggregator.util.SpreadFactorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service(Constants.LATEST_IDR_RATES)
@RequiredArgsConstructor
public class LatestIdrStrategy implements IDRDataFetcher {

    private final WebClient webClient;
    private final SpreadFactorUtil spreadFactorUtil;

    private final FinanceProperties financeProperties;
    private final GithubProperties githubUsername;

    @Override
    public Mono<UnifiedFinanceResponse> fetchData() {
        var base = financeProperties.resources().latest().base();

        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path(Constants.LATEST)
                .queryParam("base", base)
                .build()
            )
            .retrieve()
            .bodyToMono(ExternalRateResponse.class)
            .flatMap(response -> {
                if (response.rates() == null || response.rates().isEmpty()) {
                    return Mono.error(new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND));
                }

                var spreadFactor = spreadFactorUtil.calculateSpreadFactor(githubUsername.username());
                String now = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSXXX"));

                List<Map<String, Object>> allRates = response.rates().entrySet().stream()
                    .map(entry -> {
                        String currencyCode = entry.getKey();
                        var rateValue = entry.getValue();

                        Map<String, Object> detail = new LinkedHashMap<>();
                        detail.put(Constants.CURRENCY, currencyCode);
                        detail.put(Constants.RATE, rateValue);

                        var buySpread = spreadFactorUtil.calculateBuySpread(rateValue, spreadFactor);
                        String dynamicKey = currencyCode + "_BuySpread_IDR";
                        detail.put(dynamicKey, buySpread);

                        return detail;
                    })
                    .toList();

                return Mono.just(new UnifiedFinanceResponse(getResourceType(), now, allRates));
            });
    }

    @Override
    public String getResourceType() {
        return Constants.LATEST_IDR_RATES;
    }
}