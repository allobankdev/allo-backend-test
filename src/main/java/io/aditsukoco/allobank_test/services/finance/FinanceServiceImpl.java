package io.aditsukoco.allobank_test.services.finance;

import io.aditsukoco.allobank_test.models.enums.ResourceTypeEnum;
import io.aditsukoco.allobank_test.services.finance.strategy.FinanceDataFetchStrategyFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FinanceServiceImpl implements FinanceServiceInterface {

    private final FinanceDataFetchStrategyFactory financeDataFetchStrategyFactory;

    @Override
    public Object getFinanceData(ResourceTypeEnum resourceType) {
        return financeDataFetchStrategyFactory.getStrategy(resourceType).fetchData();
    }

}
