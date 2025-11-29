package com.allobank.backendtest.config;


import org.springframework.beans.factory.FactoryBean;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;

import java.util.concurrent.TimeUnit;

public class WebClientFactoryBean implements FactoryBean<WebClient> {
    private final ExternalApiProperties props;

    public WebClientFactoryBean(ExternalApiProperties props) {
        this.props = props;
    }

    @Override
    public WebClient getObject() throws Exception {
        if (props.getBaseUrl() == null || props.getBaseUrl().isBlank()) {
            throw new IllegalStateException("external.api.base-url must be configured");
        }

        int timeout = Math.max(1000, props.getTimeoutMs());

        TcpClient tcpClient = TcpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeout)
                .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(timeout, TimeUnit.MILLISECONDS)));

        HttpClient httpClient = HttpClient.from(tcpClient);

        return WebClient.builder()
                .baseUrl(props.getBaseUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader(HttpHeaders.USER_AGENT, "finance-aggregator/1.0")
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
