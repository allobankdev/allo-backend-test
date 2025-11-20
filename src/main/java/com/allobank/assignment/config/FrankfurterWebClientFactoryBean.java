package com.allobank.assignment.config;

import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Component
public class FrankfurterWebClientFactoryBean implements FactoryBean<WebClient> {

    private final FrankfurterApiProperties properties;
    private WebClient webClient;

    public FrankfurterWebClientFactoryBean(FrankfurterApiProperties properties) {
        this.properties = properties;
    }

    @Override
    public WebClient getObject() throws Exception {
        if (webClient == null) {
            Assert.hasText(properties.getBaseUrl(), "Frankfurter baseUrl must be configured");
            Duration connectTimeout = properties.getConnectionTimeout();
            Duration readTimeout = properties.getReadTimeout();

            HttpClient httpClient = HttpClient.create()
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,
                            Math.toIntExact(Math.max(0, connectTimeout.toMillis())))
                    .responseTimeout(readTimeout);

            webClient = WebClient.builder()
                    .baseUrl(properties.getBaseUrl())
                    .clientConnector(new ReactorClientHttpConnector(httpClient))
                    .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                    .build();
        }
        return webClient;
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
