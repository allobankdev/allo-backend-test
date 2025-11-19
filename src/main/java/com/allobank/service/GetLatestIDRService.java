package com.allobank.service;

import com.allobank.config.properties.ClientProperties;
import com.allobank.dto.response.GetLatestIDRResponse;
import com.allobank.exceptions.BusinessException;
import com.allobank.exceptions.ExternalException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.allobank.enums.Commons.USD;
import static com.allobank.enums.RESPONSE.*;

@Service("latest_idr_rates")
@RequiredArgsConstructor
public class GetLatestIDRService implements IDRDataFetcher<GetLatestIDRResponse>{

    private final ClientProperties clientProperties;
    private final WebClient webClient;

    @Override
    public Mono<GetLatestIDRResponse> fetch() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/latest").queryParam("base", "IDR").build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, r -> Mono.error(new BusinessException(GENERAL_ERROR)))
                .onStatus(HttpStatusCode::is5xxServerError, r -> Mono.error(new ExternalException(EXTERNAL_FAILED)))
                .bodyToMono(GetLatestIDRResponse.class)
                .flatMap(response -> {
                    BigDecimal usdRate = response.getRates().get(USD.getValue());
                    if (usdRate == null) {
                        return Mono.error(new ExternalException(RESPONSE_DOES_NOT_MATCH));
                    }

                    int sum = clientProperties.personalization().githubUsername().chars().sum();
                    double spreadFactor = (sum % 1000) / 100000.0;

                    BigDecimal usdBuySpreadIdr = BigDecimal.ONE
                            .divide(usdRate, 12, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(1.0 + spreadFactor))
                            .setScale(6, RoundingMode.HALF_UP);

                    response.setUsdBuySpreadIdr(usdBuySpreadIdr);
                    return Mono.just(response);
                });
    }


}
