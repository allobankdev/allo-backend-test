package io.aditsukoco.allobank_test.services.finance.strategy;

import io.aditsukoco.allobank_test.exceptions.BadRequestRestException;
import io.aditsukoco.allobank_test.models.enums.ResourceTypeEnum;
import io.aditsukoco.allobank_test.repositories.frankfurter.FrankfurterDataRepositoryInterface;
import io.aditsukoco.allobank_test.repositories.spreadFactor.SpreadFactorDataRepositoryInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class FinanceDataFetchStrategyFactory {
    private Map<ResourceTypeEnum, FinanceDataFetchStrategyInterface> resourceTypeToStrategy;

    public FinanceDataFetchStrategyFactory(
            FrankfurterDataRepositoryInterface frankfurterDataRepository,
            SpreadFactorDataRepositoryInterface spreadFactorDataRepository) {

        Map<ResourceTypeEnum, FinanceDataFetchStrategyInterface> resourceTypeToDataFetchStrategyMap = new HashMap<>();

        resourceTypeToDataFetchStrategyMap.put(ResourceTypeEnum.LatestIDRRates, new FetchLatestDataStrategy(frankfurterDataRepository, spreadFactorDataRepository));
        resourceTypeToDataFetchStrategyMap.put(ResourceTypeEnum.HistoricalIDRUSD, new FetchHistoricalDataStrategy(frankfurterDataRepository));

        this.resourceTypeToStrategy = resourceTypeToDataFetchStrategyMap;
    }

    public FinanceDataFetchStrategyInterface getStrategy(ResourceTypeEnum resourceType) {
        try {
            return this.resourceTypeToStrategy.get(resourceType);
        } catch (ClassCastException e) {
            throw new BadRequestRestException("unknown resource type \""+resourceType.name()+"\"");
        }
    }

}
