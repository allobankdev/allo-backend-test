package com.bezkoder.springjwt.client;

import com.bezkoder.springjwt.config.FrankfurterProperties;
import io.netty.channel.ChannelOption;
import java.time.Duration;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;

@Component
public class FrankfurterWebClientFactoryBean implements FactoryBean<WebClient> {

    private final FrankfurterProperties properties;

    public FrankfurterWebClientFactoryBean(FrankfurterProperties properties) {
        this.properties = properties;
    }

    @Override
    public WebClient getObject() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, properties.getConnectTimeoutMs())
                .responseTimeout(Duration.ofMillis(properties.getResponseTimeoutMs()));

        return WebClient.builder()
                .baseUrl(properties.getBaseUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
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
