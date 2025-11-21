package com.example.allo_bank.config;

import com.example.allo_bank.config.properties.ClientPropertiesConfig;
import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class FactoryBeanClient implements FactoryBean<WebClient> {

    private final ClientPropertiesConfig config;

    public FactoryBeanClient(ClientPropertiesConfig config) {
        this.config = config;
    }

    @Override
    public WebClient getObject() throws Exception {

        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, config.getConnectionTimeout())
                .responseTimeout(Duration.ofSeconds(config.getReadTimeout()));

        return WebClient.builder()
                .clientConnector(
                        new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Override
    public Class<?> getObjectType() {
        return WebClient.class;
    }
}
