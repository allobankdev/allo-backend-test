package io.aditsukoco.allobank_test.services.finance.strategy;

import io.aditsukoco.allobank_test.models.dto.api_response.HistoricalDataAPIResponseDTO;
import io.aditsukoco.allobank_test.repositories.frankfurter.FrankfurterDataRepositoryInterface;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FetchHistoricalDataStrategy implements FinanceDataFetchStrategyInterface {

    private final FrankfurterDataRepositoryInterface frankfurterDataRepository;

    @Override
    public HistoricalDataAPIResponseDTO fetchData() {
        return frankfurterDataRepository.getHistoricalResponseData();
    }
}
