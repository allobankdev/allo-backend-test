package com.allobank.allo_backend_test.finance.service.strategy.finance_resource;

import com.allobank.allo_backend_test.finance.client.DataSourceClient;
import com.allobank.allo_backend_test.finance.model.FinanceResource;
import com.allobank.allo_backend_test.finance.model.LatestRatesModel;
import com.allobank.allo_backend_test.finance.repository.FinanceRepository;
import com.allobank.allo_backend_test.finance.service.SpreadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LatestIdrRatesHandler implements FinanceResourceHandler {

    private final DataSourceClient client;
    private final FinanceRepository repository;
    private final SpreadService spreadService;

    @Override
    public String resourceType() {
        return "latest_idr_rates";
    }

    @Override
    public FinanceResource fetch() {
        var dto = client.getLatestRates("IDR");

        Double spread = spreadService.calculateSpread(dto.rates().get("USD"));
        LatestRatesModel model = new LatestRatesModel(
                dto.amount(), dto.base(), dto.date(), dto.rates(), spread);
        repository.put(resourceType(), model);
        return model;
    }

    @Override
    public FinanceResource get() {
        return repository.get(resourceType());
    }
}