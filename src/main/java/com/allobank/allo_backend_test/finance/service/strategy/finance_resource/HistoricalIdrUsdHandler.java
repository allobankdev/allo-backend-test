package com.allobank.allo_backend_test.finance.service.strategy.finance_resource;

import com.allobank.allo_backend_test.finance.client.DataSourceClient;
import com.allobank.allo_backend_test.finance.model.FinanceResource;
import com.allobank.allo_backend_test.finance.model.HistoricalRatesModel;
import com.allobank.allo_backend_test.finance.repository.FinanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HistoricalIdrUsdHandler implements FinanceResourceHandler {

    private final DataSourceClient client;
    private final FinanceRepository repository;

    @Override
    public String resourceType() {
        return "historical_idr_usd";
    }

    @Override
    public FinanceResource fetch() {
        var dto = client.getHistoricalRates("2024-01-01", "2024-01-05", "IDR", "USD");
        HistoricalRatesModel model = new HistoricalRatesModel(
                dto.amount(), dto.base(), dto.startDate(), dto.endDate(), dto.rates());
        repository.put(resourceType(), model);
        return model;
    }

    @Override
    public FinanceResource get() {
        return repository.get(resourceType());
    }
}