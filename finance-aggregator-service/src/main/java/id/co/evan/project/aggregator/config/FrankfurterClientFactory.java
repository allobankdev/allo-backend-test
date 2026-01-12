package id.co.evan.project.aggregator.config;

import id.co.evan.project.aggregator.config.properties.FrankfurterProperties;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class FrankfurterClientFactory implements FactoryBean<WebClient> {

    private final FrankfurterProperties frankfurterProperties;

    @Override
    public WebClient getObject() {

        var connectTimeout = frankfurterProperties.connectTimeoutMs();
        var responseTimeout = frankfurterProperties.responseTimeoutMs();

        var httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout)
            .responseTimeout(Duration.ofMillis(responseTimeout))
            .doOnConnected(conn -> conn
                .addHandlerLast(new ReadTimeoutHandler(responseTimeout, TimeUnit.MILLISECONDS))
                .addHandlerLast(new WriteTimeoutHandler(responseTimeout, TimeUnit.MILLISECONDS))
            );

        return WebClient.builder()
            .baseUrl(frankfurterProperties.baseUrl())
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .filter(ExchangeFilterFunction.ofRequestProcessor(Mono::just))
            .build();
    }

    @Override
    public Class<WebClient> getObjectType() {
        return WebClient.class;
    }
}