package com.bank.allo.client;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import java.time.Duration;

public class FrankfurterWebClientFactoryBean implements FactoryBean<WebClient> {

    private final String baseUrl;

    public FrankfurterWebClientFactoryBean(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public WebClient getObject() {
        HttpClient client = HttpClient.create().responseTimeout(Duration.ofSeconds(10));

        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(c -> c.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                .build();

        return WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(client))
                .exchangeStrategies(strategies)
                .build();
    }

    @Override
    public Class<?> getObjectType() {
        return WebClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
