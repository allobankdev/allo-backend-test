package com.allobank.finance.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * FactoryBean khusus untuk membuat instance WebClient yang terkoneksi ke
 * Frankfurter API.
 *
 * <p>
 * Mengimplementasikan {@link FactoryBean} sesuai Constraint B:
 * - Eksternalisasi Base URL via {@link FrankfurterProperties}
 * - Konfigurasi timeout (connect + read + write)
 * - Menetapkan default header Accept: application/json
 *
 * <p>
 * Tidak menggunakan @Bean langsung di @Configuration melainkan FactoryBean
 * agar konfigurasi client lebih terpusat, testable, dan memisahkan tanggung
 * jawab
 * pembuatan client dari konfigurasi aplikasi umum.
 */
@Setter
public class WebClientFactoryBean implements FactoryBean<WebClient> {

    private final FrankfurterProperties properties;

    public WebClientFactoryBean(FrankfurterProperties properties) {
        this.properties = properties;
    }

    @Override
    public WebClient getObject() {
        int timeoutSeconds = properties.getTimeoutSeconds();

        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, (int) Duration.ofSeconds(timeoutSeconds).toMillis())
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(timeoutSeconds, TimeUnit.SECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(timeoutSeconds, TimeUnit.SECONDS)));

        return WebClient.builder()
                .baseUrl(properties.getBaseUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public Class<?> getObjectType() {
        return WebClient.class;
    }

    /**
     * Singleton: satu instance WebClient dipakai ulang di seluruh aplikasi.
     */
    @Override
    public boolean isSingleton() {
        return true;
    }
}
