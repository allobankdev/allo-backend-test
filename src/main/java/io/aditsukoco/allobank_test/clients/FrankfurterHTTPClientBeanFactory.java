package io.aditsukoco.allobank_test.clients;

import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class FrankfurterHTTPClientBeanFactory implements FactoryBean<FrankfurterHTTPClientInterface> {

    @Override
    public @Nullable FrankfurterHTTPClientInterface getObject() throws Exception {
        return new FrankfurterHTTPClientImpl();
    }

    @Override
    public @Nullable Class<?> getObjectType() {
        return FrankfurterHTTPClientInterface.class;
    }
}
