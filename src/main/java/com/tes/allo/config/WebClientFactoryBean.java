package com.tes.allo.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

public class WebClientFactoryBean implements FactoryBean<WebClient>, InitializingBean {

    private final FrankfurterProperties props;
    private WebClient webClient;

    public WebClientFactoryBean(FrankfurterProperties props) {
        this.props = props;
    }

    @Override
    public WebClient getObject() {
        return webClient;
    }

    @Override
    public Class<?> getObjectType() {
        return WebClient.class;
    }

    @Override
    public void afterPropertiesSet() {
        TcpClient tcpClient = TcpClient.create()
                .option(io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS, props.getConnectTimeoutMs())
                .doOnConnected(conn ->
                        conn.addHandlerLast(
                                new io.netty.handler.timeout.ReadTimeoutHandler(
                                        Math.max(1, props.getReadTimeoutMs() / 1000))));

        HttpClient httpClient = HttpClient.from(tcpClient);

        this.webClient = WebClient.builder()
                .baseUrl(props.getBaseUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(c -> c.defaultCodecs().maxInMemorySize(10 * 1024 * 1024))
                        .build())
                .build();
    }
}
