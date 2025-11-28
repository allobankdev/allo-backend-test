package com.project.alloBank.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Component("frankfurterClient")
public class WebClientFactoryBean implements FactoryBean<WebClient>{

    @Value("${app.frankfurter.base-url}")
    private String baseUrl;

    private WebClient webClient;

    @Override
    public WebClient getObject() throws Exception {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(5));
        return WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader(HttpHeaders.ACCEPT,"application/json")
                .build();
    }

    @Override
    public Class<?> getObjectType() {
        return WebClient.class;
    }

}
