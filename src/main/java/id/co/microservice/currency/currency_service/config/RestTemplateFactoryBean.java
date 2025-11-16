package id.co.microservice.currency.currency_service.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class RestTemplateFactoryBean implements FactoryBean<RestTemplate> {

    private final ExternalApiConfig externalApiConfig;

    public RestTemplateFactoryBean(ExternalApiConfig externalApiConfig) {
        this.externalApiConfig = externalApiConfig;
    }

    @Override
    public boolean isSingleton() {
        return FactoryBean.super.isSingleton();
    }

    @Override
    public Class<?> getObjectType() {
        return RestTemplate.class;
    }

    @Override
    public RestTemplate getObject() throws Exception {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(externalApiConfig.getConnectTimeout());
        factory.setReadTimeout(externalApiConfig.getReadTimeout());

        RestTemplate restTemplate = new RestTemplate(factory);
        return restTemplate;
    }
}
