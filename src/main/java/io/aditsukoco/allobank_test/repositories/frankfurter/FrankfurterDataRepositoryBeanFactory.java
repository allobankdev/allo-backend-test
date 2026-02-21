package io.aditsukoco.allobank_test.repositories.frankfurter;

import io.aditsukoco.allobank_test.clients.frankfurter.FrankfurterHTTPClientInterface;
import io.aditsukoco.allobank_test.models.dto.api_response.LatestAPIResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FrankfurterDataRepositoryBeanFactory implements FactoryBean<FrankfurterDataRepositoryInterface> {

    // Clients
    private final ObjectProvider<FrankfurterHTTPClientInterface> frankfurterHTTPClient;

    @Override
    public @Nullable FrankfurterDataRepositoryInterface getObject() throws Exception {
        FrankfurterDataRepositoryImpl repo = new FrankfurterDataRepositoryImpl(frankfurterHTTPClient.getObject());
        LatestAPIResponseDTO data = repo.frankfurterHTTPClientInterface.fetchLatest(1, "IDR", "USD");
        repo.setLatestResponseData(data);
        return repo;
    }

    @Override
    public @Nullable Class<?> getObjectType() {
        return FrankfurterDataRepositoryInterface.class;
    }
}
