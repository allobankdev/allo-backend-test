package com.htc.allobank.config;

import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

@Component
public class FrankfurterWebClientFactoryBean implements FactoryBean<WebClient> {

    private final ExternalApiProperties props;

    public FrankfurterWebClientFactoryBean(ExternalApiProperties props) {
        this.props = props;
    }

    @Override
    public WebClient getObject() {
        TcpClient tcpClient = TcpClient.create()
          .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,
            props.getFrankfurter().getTimeoutMs());

        HttpClient httpClient = HttpClient.from(tcpClient);

        return WebClient.builder()
          .baseUrl(props.getFrankfurter().getBaseUrl())
          .clientConnector(new ReactorClientHttpConnector(httpClient))
          .defaultHeader("Accept", "application/json")
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
