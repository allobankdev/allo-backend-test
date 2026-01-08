package org.allobanktest.client;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
public class FrankfurterClientFactory implements FactoryBean<WebClient> {
    private final FrankfurterProperties properties;

    @Override
    public @Nullable WebClient getObject() throws Exception {
        return WebClient.builder()
                .baseUrl(properties.getBaseURL())
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer ->
                                configurer.defaultCodecs().maxInMemorySize(1024 * 1024))
                        .build())
                .build();
    }

    @Override
    public @Nullable Class<?> getObjectType() {
        return WebClient.class;
    }
}
