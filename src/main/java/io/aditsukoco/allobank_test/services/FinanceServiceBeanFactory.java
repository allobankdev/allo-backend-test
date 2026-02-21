package io.aditsukoco.allobank_test.services;

import io.aditsukoco.allobank_test.repositories.FrankfurterDataRepositoryInterface;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FinanceServiceBeanFactory implements FactoryBean<FinanceServiceInterface> {

    private final ObjectProvider<FrankfurterDataRepositoryInterface> frankfurterDataRepositoryInterface;

    @Override
    public @Nullable FinanceServiceInterface getObject() throws Exception {
        return new FinanceServiceImpl(frankfurterDataRepositoryInterface.getObject());
    }

    @Override
    public @Nullable Class<?> getObjectType() {
        return FinanceServiceInterface.class;
    }
}
