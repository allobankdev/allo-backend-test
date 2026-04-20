package id.allobank.exchangerate.client;

import id.allobank.exchangerate.config.ExternalFrankfurterProperties;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Component
@RequiredArgsConstructor
@Slf4j
public class FrankfurterClientFactory implements FactoryBean<WebClient> {

    private final ExternalFrankfurterProperties props;

    @Override
    public @Nullable WebClient getObject() throws Exception {
        log.info("WebClient initialized with baseUrl={}", props.getBaseUrl());

        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, props.getTimeout().getConnectMs()) // connect timeout
                .responseTimeout(Duration.ofSeconds(props.getTimeout().getResponseMs()))              // response timeout
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(props.getTimeout().getReadSec()))
                                .addHandlerLast(new WriteTimeoutHandler(props.getTimeout().getWriteSec())));

        return WebClient.builder()
                .baseUrl(props.getBaseUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Override
    public @Nullable Class<?> getObjectType() {
        return WebClient.class;
    }

    @Override
    public boolean isSingleton() {
        return FactoryBean.super.isSingleton();
    }
}
