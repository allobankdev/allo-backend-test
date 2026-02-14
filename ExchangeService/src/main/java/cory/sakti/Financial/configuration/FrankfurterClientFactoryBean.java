package cory.sakti.Financial.configuration;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.web.client.RestTemplate;

public class FrankfurterClientFactoryBean implements FactoryBean<RestTemplate> {
    @Override
    public @Nullable RestTemplate getObject() throws Exception {
        return null;
    }

    @Override
    public @Nullable Class<?> getObjectType() {
        return RestTemplate.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
