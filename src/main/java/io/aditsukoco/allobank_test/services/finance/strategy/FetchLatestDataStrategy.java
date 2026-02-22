package io.aditsukoco.allobank_test.services.finance.strategy;

import io.aditsukoco.allobank_test.models.dto.api_response.LatestAPIResponseDTO;
import io.aditsukoco.allobank_test.models.dto.response.LatestDataResponseDTO;
import io.aditsukoco.allobank_test.repositories.frankfurter.FrankfurterDataRepositoryInterface;
import io.aditsukoco.allobank_test.repositories.spreadFactor.SpreadFactorDataRepositoryInterface;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FetchLatestDataStrategy implements FinanceDataFetchStrategyInterface {

    private final FrankfurterDataRepositoryInterface frankfurterDataRepository;
    private final SpreadFactorDataRepositoryInterface spreadFactorDataRepository;

    @Override
    public LatestDataResponseDTO fetchData() {
        LatestAPIResponseDTO apiResponse = this.frankfurterDataRepository.getLatestResponseData();

        // Final Formula: USD_BuySpread_IDR = (1 / Rate_USD) * (1 + Spread Factor) (where Rate_USD is the value from the API when base=IDR).
        double spreadFactor =  this.spreadFactorDataRepository.getSpreadFactor();
        float rateUSD = apiResponse.getRates().get("USD");
        double result = (1 / rateUSD) * (1 + spreadFactor);

        return LatestDataResponseDTO.build(apiResponse, result);
    }
}
