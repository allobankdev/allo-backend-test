package com.amri.apiintegration.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component("frankfurterRestClient")
@RequiredArgsConstructor
public class FrankfurterRestClientFactoryBean implements FactoryBean<RestClient> {

    private final FrankfurterProperties frankfurterProperties;
    private volatile RestClient restClient;

    @Override
    public RestClient getObject() {
        if (restClient == null) {
            synchronized (this) {
                if (restClient == null) {
                    SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
                    requestFactory.setConnectTimeout(frankfurterProperties.getConnectTimeoutMillis());
                    requestFactory.setReadTimeout(frankfurterProperties.getReadTimeoutMillis());

                    restClient = RestClient.builder()
                            .baseUrl(frankfurterProperties.getBaseUrl())
                            .defaultHeader(HttpHeaders.USER_AGENT, frankfurterProperties.getUserAgent())
                            .requestFactory(requestFactory)
                            .build();
                }
            }
        }
        return restClient;
    }

    @Override
    public Class<?> getObjectType() {
        return RestClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
