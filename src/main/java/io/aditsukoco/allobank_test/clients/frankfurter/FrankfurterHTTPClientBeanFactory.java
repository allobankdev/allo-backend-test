package io.aditsukoco.allobank_test.clients.frankfurter;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Component
public class FrankfurterHTTPClientBeanFactory implements FactoryBean<FrankfurterHTTPClientInterface> {
    @Value("${frankfurter.base_url}")
    private String frankfurterBaseUrl;

    @Override
    public @Nullable FrankfurterHTTPClientInterface getObject() throws Exception {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        restTemplateBuilder.readTimeout(Duration.ofSeconds(30L));
        RestTemplate restTemplate = restTemplateBuilder.build();

        return new FrankfurterHTTPClientImpl(frankfurterBaseUrl, restTemplate);
    }

    @Override
    public @Nullable Class<?> getObjectType() {
        return FrankfurterHTTPClientInterface.class;
    }
}
