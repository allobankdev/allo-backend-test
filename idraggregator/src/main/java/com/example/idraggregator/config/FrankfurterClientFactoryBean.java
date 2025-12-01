package com.example.idraggregator.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.tcp.TcpClient;
import java.time.Duration;

@Component
public class FrankfurterClientFactoryBean implements FactoryBean<WebClient> {

    private final FrankfurterProperties properties;

    @Autowired
    public FrankfurterClientFactoryBean(FrankfurterProperties properties) {
        this.properties = properties;
    }

    @Override
    public WebClient getObject() {
        // configure Reactor Netty HttpClient with timeouts from properties
        TcpClient tcpClient = TcpClient.create(ConnectionProvider.create("frankfurter-pool"))
                .option(io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS, (int) properties.getTimeouts().getConnectMillis())
                .doOnConnected(conn -> conn
                        .addHandlerLast(new io.netty.handler.timeout.ReadTimeoutHandler((int) properties.getTimeouts().getReadMillis() / 1000))
                );

        HttpClient httpClient = HttpClient.from(tcpClient);

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
