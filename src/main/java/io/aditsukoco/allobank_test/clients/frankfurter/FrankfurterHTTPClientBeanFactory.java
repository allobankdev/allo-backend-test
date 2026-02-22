package io.aditsukoco.allobank_test.clients.frankfurter;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FrankfurterHTTPClientBeanFactory implements FactoryBean<FrankfurterHTTPClientInterface> {
    @Value("${frankfurter.base_url}")
    private String frankfurterBaseUrl;

    @Override
    public @Nullable FrankfurterHTTPClientInterface getObject() throws Exception {
        return new FrankfurterHTTPClientImpl(frankfurterBaseUrl);
    }

    @Override
    public @Nullable Class<?> getObjectType() {
        return FrankfurterHTTPClientInterface.class;
    }
}
