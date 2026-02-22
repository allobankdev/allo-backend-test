package io.aditsukoco.allobank_test.repositories.frankfurter;

import io.aditsukoco.allobank_test.clients.frankfurter.FrankfurterHTTPClientInterface;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FrankfurterDataRepositoryBeanFactory implements FactoryBean<FrankfurterDataRepositoryInterface> {

    // Clients
    private final ObjectProvider<FrankfurterHTTPClientInterface> frankfurterHTTPClient;

    @Override
    public @Nullable FrankfurterDataRepositoryInterface getObject() throws Exception {
        return new FrankfurterDataRepositoryImpl(frankfurterHTTPClient.getObject());
    }

    @Override
    public @Nullable Class<?> getObjectType() {
        return FrankfurterDataRepositoryInterface.class;
    }
}
