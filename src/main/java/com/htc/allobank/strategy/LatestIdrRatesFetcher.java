package com.htc.allobank.strategy;

import com.htc.allobank.util.SpreadUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component("latest_idr_rates")
@AllArgsConstructor
public class LatestIdrRatesFetcher implements IDRDataFetcher {

    private final WebClient client;
    private final SpreadUtil spreadUtil;

    @Override
    public Mono<Object> fetch() {
        return client.get()
          .uri(uriBuilder -> uriBuilder.path("/latest")
            .queryParam("base", "IDR")
            .build())
          .retrieve()
          .bodyToMono(Map.class)
          .map(map -> {
              Map<String, Object> rates = (Map<String, Object>) map.get("rates");
              Object rateUsdObj = rates.get("USD");
              double rateUsd = ((Number) rateUsdObj).doubleValue();

              double spreadFactor = spreadUtil.computeSpreadFactor();
              double usdBuySpreadIdr = (1.0 / rateUsd) * (1 + spreadFactor);

              map.put("USD_BuySpread_IDR", usdBuySpreadIdr);
              map.put("SpreadFactor", spreadFactor);
              return map;
          });
    }
}
