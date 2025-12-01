package com.example.idraggregator.service.strategy;

import com.example.idraggregator.config.FrankfurterClientFactoryBean;
import com.example.idraggregator.dto.SupportedCurrenciesDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Fetches /currencies
 */
@Component("supported_currencies")
public class SupportedCurrenciesFetcher implements IDRDataFetcher<SupportedCurrenciesDto> {

    private final WebClient webClient;

    @Autowired
    public SupportedCurrenciesFetcher(FrankfurterClientFactoryBean clientFactoryBean) {
        this.webClient = (WebClient) clientFactoryBean.getObject();
    }

    @Override
    public SupportedCurrenciesDto fetch() throws Exception {
        Mono<SupportedCurrenciesDto> mono = webClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(SupportedCurrenciesDto.class);

        SupportedCurrenciesDto dto = mono.block();
        if (dto == null) throw new IllegalStateException("No currencies payload");
        return dto;
    }

    @Override
    public String resourceKey() {
        return "supported_currencies";
    }
}
