package com.allobank.test.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Dibuat oleh: Andre Rizaldi Brillianto
 * Email: andrerizaldib@gmail.com
 * Date: Wednesday, 7-January-2026
 * description: allo-bank-test
 */

@Component
public class FrankfurterClientFactory implements FactoryBean<WebClient> {

    // base url, from frankfurter.api.base-url
    @Value("${frankfurter.api.base-url}")
    private String baseUrl;

    @Override
    public WebClient getObject() throws Exception {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    // ngasih tau kalo objek yang diproduksi adalah bertipe Webclient
    @Override
    public Class<?> getObjectType() {
        return WebClient.class;
    }

    // singleton
    @Override
    public boolean isSingleton() {
        return true;
    }
}
