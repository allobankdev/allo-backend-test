package com.allobank.aggregator.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;

import java.time.Duration;

@Component("frankfurterClientFactory")
public class FrankfurterWebClientFactoryBean implements FactoryBean<WebClient> {

    private final FrankfurterProperties props;

    @Autowired
    public FrankfurterWebClientFactoryBean(FrankfurterProperties props) {
        this.props = props;
    }

    @Override
    public WebClient getObject() {
        ConnectionProvider provider = ConnectionProvider.create("frankfurter-conn");
        HttpClient httpClient = HttpClient.create(provider)
                .responseTimeout(Duration.ofMillis(props.getTimeoutMs()));

        ReactorClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);

        return WebClient.builder()
                .baseUrl(props.getBaseUrl())
                .clientConnector(connector)
                .exchangeStrategies(ExchangeStrategies.builder().build())
                .build();
    }

    @Override
    public Class<?> getObjectType() { return WebClient.class; }

    @Override
    public boolean isSingleton() { return true; }
}
