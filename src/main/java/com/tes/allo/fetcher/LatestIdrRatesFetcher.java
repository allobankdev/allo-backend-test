package com.tes.allo.fetcher;


import com.tes.allo.config.FrankfurterProperties;
import com.tes.allo.dto.LatestRatesDto;
import com.tes.allo.util.SpreadCalculator;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

public class LatestIdrRatesFetcher implements IDRDataFetcher {

    private final WebClient webClient;
    private final FrankfurterProperties props;

    public LatestIdrRatesFetcher(WebClient webClient, FrankfurterProperties props) {
        this.webClient = webClient;
        this.props = props;
    }

    @Override
    public Object fetch() {
        Map<String, Object> resp = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/latest").queryParam("base","IDR").build())
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> clientResponse.createException().flatMap(Mono::error))
                .bodyToMono(Map.class)
                .block();

        if (resp == null) throw new RuntimeException("Empty latest response");
        String base = (String) resp.get("base");
        String date = (String) resp.get("date");
        Map<String, Double> rates = (Map) resp.get("rates");
        Double rateUsd = rates.get("USD");
        if (rateUsd == null) throw new RuntimeException("USD rate missing");

        double spreadFactor = SpreadCalculator.computeSpreadFactor(props.getGithubUsername());
        double idrPerUsd = 1.0 / rateUsd;
        double usdBuySpreadIdr = idrPerUsd * (1.0 + spreadFactor);

        return new LatestRatesDto(base, date, rates, usdBuySpreadIdr, spreadFactor);
    }

    @Override
    public String key() { return "latest_idr_rates"; }
}
