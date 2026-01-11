package com.prasetyahs.allo.finance.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class FrankfurterClientFactoryBean implements FactoryBean<WebClient> {

    private final String baseUrl;

    public FrankfurterClientFactoryBean(@Value("${app.frankfurter.base-url}") String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public WebClient getObject() throws Exception {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024)) // 16MB buffer just
                                                                                                    // in case
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
