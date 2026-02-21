package io.aditsukoco.allobank_test.services.finance.strategy;

import io.aditsukoco.allobank_test.repositories.frankfurter.FrankfurterDataRepositoryInterface;
import io.aditsukoco.allobank_test.repositories.spreadFactor.SpreadFactorDataRepositoryInterface;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FinanceDataFetchStrategyFactoryBeanFactory implements FactoryBean<FinanceDataFetchStrategyFactory> {

    private final ObjectProvider<FrankfurterDataRepositoryInterface> frankfurterDataRepositoryObjectProvider;
    private final ObjectProvider<SpreadFactorDataRepositoryInterface> spreadFactorDataRepositoryObjectProvider;

    @Override
    public @Nullable FinanceDataFetchStrategyFactory getObject() throws Exception {
        return new FinanceDataFetchStrategyFactory(
                frankfurterDataRepositoryObjectProvider.getObject(),
                spreadFactorDataRepositoryObjectProvider.getObject()
        );
    }

    @Override
    public @Nullable Class<?> getObjectType() {
        return null;
    }
}
