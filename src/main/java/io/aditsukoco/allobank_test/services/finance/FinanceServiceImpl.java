package io.aditsukoco.allobank_test.services.finance;

import io.aditsukoco.allobank_test.models.dto.api_response.LatestAPIResponseDTO;
import io.aditsukoco.allobank_test.models.enums.ResourceTypeEnum;
import io.aditsukoco.allobank_test.repositories.frankfurter.FrankfurterDataRepositoryInterface;
import io.aditsukoco.allobank_test.repositories.spreadFactor.SpreadFactorDataRepositoryInterface;
import io.aditsukoco.allobank_test.services.finance.strategy.FinanceDataFetchStrategyFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FinanceServiceImpl implements FinanceServiceInterface {

    private final FrankfurterDataRepositoryInterface frankfurterDataRepository;
    private final SpreadFactorDataRepositoryInterface spreadFactorDataRepository;
    private final FinanceDataFetchStrategyFactory financeDataFetchStrategyFactory;

    @Override
    public Object getFinanceData(ResourceTypeEnum resourceType) {
        return financeDataFetchStrategyFactory.getStrategy(resourceType).fetchData();
    }

}
