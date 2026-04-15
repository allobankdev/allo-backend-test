package id.co.allobank.exchangerate.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WebClientFactory implements FactoryBean<WebClient> {

    @Value("${frankfurter.base-url}")
    private String baseUrl;

    @Override
    public WebClient getObject() {
        return WebClient.builder()
                .baseUrl("https://api.frankfurter.dev/v1")
                .build();
    }

    @Override
    public Class<?> getObjectType() {
        return WebClient.class;
    }
}