package id.allobank.exchangerate.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class CurrencyStrategy implements IDRDataFetcher {

    private final WebClient webClient;

    @Override
    public String getType() {
        return "supported_currencies";
    }

    @Override
    public Object fetch() {
        return webClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }
}