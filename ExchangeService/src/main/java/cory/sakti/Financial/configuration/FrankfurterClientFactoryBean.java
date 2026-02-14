package cory.sakti.Financial.configuration;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class FrankfurterClientFactoryBean implements FactoryBean<RestTemplate> {
    private final String baseUrl;

    // Externalizing the API Base URL via @Value
    public FrankfurterClientFactoryBean(@Value("${app.api.base-url}") String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public RestTemplate getObject() throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(baseUrl));

        return restTemplate;
    }

    @Override
    public Class<?> getObjectType() {
        return RestTemplate.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
