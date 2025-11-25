package com.example.financedata.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.tcp.TcpClient;

import java.time.Duration;

@Component
public class FrankfurterWebClientFactoryBean implements FactoryBean<WebClient> {

    private final FrankfurterProperties properties;
    private WebClient instance;

    @Autowired
    public FrankfurterWebClientFactoryBean(FrankfurterProperties properties) {
        this.properties = properties;
        this.instance = createClient();
    }

    private WebClient createClient() {
        // small connection pool and timeouts for production readiness
        ConnectionProvider provider = ConnectionProvider.builder("frank-client-pool")
                .maxConnections(100)
                .pendingAcquireMaxCount(200)
                .build();

        TcpClient tcpClient = TcpClient.create()
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
        .doOnConnected(conn -> conn
            .addHandlerLast(new ReadTimeoutHandler(30))
            .addHandlerLast(new WriteTimeoutHandler(30)));


        HttpClient httpClient = HttpClient.from(tcpClient)
                .responseTimeout(Duration.ofSeconds(30));

        return WebClient.builder()
                .baseUrl(properties.getBaseUrl())
                .clientConnector(new org.springframework.http.client.reactive.ReactorClientHttpConnector(httpClient))
                .defaultHeader("Accept", "application/json")
                .build();
    }

    @Override
    public WebClient getObject() {
        return instance;
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
