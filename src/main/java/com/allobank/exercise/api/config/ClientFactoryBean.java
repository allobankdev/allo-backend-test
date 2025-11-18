package com.allobank.exercise.api.config;

import com.allobank.exercise.api.properties.ClientProperties;
import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import java.time.Duration;

@Configuration
public class ClientFactoryBean implements FactoryBean<WebClient> {

    private final ClientProperties clientProperties;

    public ClientFactoryBean(ClientProperties clientProperties) {
        this.clientProperties = clientProperties;
    }

    @Override
    public WebClient getObject() throws Exception {

        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, clientProperties.getConnectTimeout())
                .responseTimeout(Duration.ofSeconds(clientProperties.getReadTimeout()));

        return WebClient.builder()
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
