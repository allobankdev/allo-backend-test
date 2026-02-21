package io.aditsukoco.allobank_test.services;

import io.aditsukoco.allobank_test.repositories.FrankfurterDataRepositoryInterface;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FinanceServiceImpl implements FinanceServiceInterface {

    private final FrankfurterDataRepositoryInterface frankfurterDataRepository;

    @Override
    public String getLatestData() {
        return "";
    }

}
