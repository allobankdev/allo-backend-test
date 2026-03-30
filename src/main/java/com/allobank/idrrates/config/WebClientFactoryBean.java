package com.allobank.idrrates.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Component
public class WebClientFactoryBean implements FactoryBean<WebClient> {

    private static final Logger log = LoggerFactory.getLogger(WebClientFactoryBean.class);

    @Value("${frankfurter.baseurl}")
    private String baseUrl;

    @Value("${frankfurter.timeout}")
    private int timeoutSeconds;

    @Override
    public WebClient getObject() {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(timeoutSeconds));
        log.info("Initializing webcllient with base URL : {} and timeout {}s", baseUrl, timeoutSeconds);
        return WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .codecs(clientCodecConfigurer -> clientCodecConfigurer
                        .defaultCodecs()
                        .maxInMemorySize(1024 * 1024))
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
