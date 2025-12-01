package com.app.config.factory;

import com.app.config.properties.ApiClientProperties;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
public class RestTemplateFactoryBean implements FactoryBean<RestTemplate> {

    private final ApiClientProperties apiClientProperties;
    private RestTemplate restTemplate;

    public RestTemplateFactoryBean(ApiClientProperties apiClientProperties) {
        this.apiClientProperties = apiClientProperties;
    }

    @Override
    public RestTemplate getObject() throws Exception {
        if (restTemplate == null){
            SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
            factory.setConnectTimeout(apiClientProperties.getConnectionTimeout());
            factory.setReadTimeout(apiClientProperties.getReadTimeout());

            RestTemplate template = new RestTemplate(factory);

            // Add shared headers via interceptor
            List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>(template.getInterceptors());

            interceptors.add((request, body, execution) -> {
                //request.getHeaders().add("User-Agent", apiClientProperties.getDefaultUserAgent());
                // add other shared headers if needed
                return execution.execute(request, body);
            });

            template.setInterceptors(interceptors);

            this.restTemplate = template;

        }

        return restTemplate;
    }

    @Override
    public Class<?> getObjectType() {
        return RestTemplate.class;
    }

}


