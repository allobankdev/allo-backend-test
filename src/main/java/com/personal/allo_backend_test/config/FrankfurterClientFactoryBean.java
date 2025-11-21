package com.personal.allo_backend_test.config;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import com.personal.allo_backend_test.properties.FrankfurterClientProperties;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.netty.http.client.HttpClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class FrankfurterClientFactoryBean implements FactoryBean<WebClient> {

  private static final long DEFAULT_TIMEOUT_MILLIS = TimeUnit.SECONDS.toMillis(5);

  private final FrankfurterClientProperties frankfurterClientProperties;

  @Override
  public WebClient getObject() {
    return WebClient.builder()
      .clientConnector(createClientConnector(
        frankfurterClientProperties.getReadTimeout(),
        frankfurterClientProperties.getWriteTimeout(),
        frankfurterClientProperties.getConnectTimeout()))
      .baseUrl(frankfurterClientProperties.getBaseUrl())
      .exchangeStrategies(ExchangeStrategies.builder()
        .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(
          frankfurterClientProperties.getMaxInMemorySizeInMb() * 1024 * 1024))
          .build())
      .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
      .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
  }

  public static ClientHttpConnector createClientConnector(Duration readTimeout,
      Duration writeTimeout, Duration connectionTimeout) {
    return new ReactorClientHttpConnector(HttpClient.create()
      .tcpConfiguration(client -> client
        .doOnConnected(connection -> connection
          .addHandlerLast(new ReadTimeoutHandler(getMillis(readTimeout), TimeUnit.MILLISECONDS))
          .addHandlerLast(new WriteTimeoutHandler(getMillis(writeTimeout), TimeUnit.MILLISECONDS)))
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, (int) getMillis(connectionTimeout)))
      .wiretap(true));
  }

  private static long getMillis(Duration duration) {
    return Optional.ofNullable(duration)
      .map(Duration::toMillis)
      .orElse(DEFAULT_TIMEOUT_MILLIS);
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

