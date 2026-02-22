package io.aditsukoco.allobank_test.services.finance.strategy;

import io.aditsukoco.allobank_test.repositories.frankfurter.FrankfurterDataRepositoryInterface;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class FetchCurrenciesDataStrategy implements FinanceDataFetchStrategyInterface {

    private final FrankfurterDataRepositoryInterface frankfurterDataRepository;

    @Override
    public Map<String, String> fetchData() {
        return frankfurterDataRepository.getCurrencies();
    }
}
