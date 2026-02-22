package io.aditsukoco.allobank_test.services.finance;

import io.aditsukoco.allobank_test.services.finance.strategy.FinanceDataFetchStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FinanceServiceBeanFactory implements FactoryBean<FinanceServiceInterface> {

    private final ObjectProvider<FinanceDataFetchStrategyFactory> financeDataFetchStrategyFactoryObjectProvider;

    @Override
    public @Nullable FinanceServiceInterface getObject() throws Exception {
        return new FinanceServiceImpl(
                financeDataFetchStrategyFactoryObjectProvider.getObject()
        );
    }

    @Override
    public @Nullable Class<?> getObjectType() {
        return FinanceServiceInterface.class;
    }
}
