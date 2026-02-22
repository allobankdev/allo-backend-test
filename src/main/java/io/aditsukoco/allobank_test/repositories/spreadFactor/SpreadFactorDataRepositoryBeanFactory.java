package io.aditsukoco.allobank_test.repositories.spreadFactor;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SpreadFactorDataRepositoryBeanFactory implements FactoryBean<SpreadFactorDataRepositoryInterface> {

    @Value("${github.username}")
    private String githubUsername;

    @Override
    public @Nullable SpreadFactorDataRepositoryInterface getObject() throws Exception {
        return new SpreadFactorDataRepositoryImpl(githubUsername);
    }

    @Override
    public @Nullable Class<?> getObjectType() {
        return SpreadFactorDataRepositoryInterface.class;
    }
}
