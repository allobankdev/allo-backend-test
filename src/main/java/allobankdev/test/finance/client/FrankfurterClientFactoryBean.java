package allobankdev.test.finance.client;

import allobankdev.test.finance.config.FrankfurterProperties;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class FrankfurterClientFactoryBean  implements FactoryBean<FrankfurterClient> {

    private final FrankfurterProperties properties;

    public FrankfurterClientFactoryBean(FrankfurterProperties properties) {
        this.properties = properties;
    }

    @Override
    public FrankfurterClient getObject() {
        RestTemplate restTemplate = new RestTemplate();
        return new FrankfurterClient(restTemplate, properties.getBaseUrl());
    }

    @Override
    public Class<?> getObjectType() {
        return FrankfurterClient.class;
    }
}
