package com.allobank.allo_backend_test.finance.service.strategy.finance_resource;

import com.allobank.allo_backend_test.finance.client.DataSourceClient;
import com.allobank.allo_backend_test.finance.model.CurrenciesModel;
import com.allobank.allo_backend_test.finance.model.FinanceResource;
import com.allobank.allo_backend_test.finance.repository.FinanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SupportedCurrenciesHandler implements FinanceResourceHandler {

    private final DataSourceClient client;
    private final FinanceRepository repository;

    @Override
    public String resourceType() {
        return "supported_currencies";
    }

    @Override
    public FinanceResource fetch() {
        var dto = client.getCurrencies();
        CurrenciesModel model = new CurrenciesModel(dto);
        repository.put(resourceType(), model);
        return model;
    }

    @Override
    public FinanceResource get() {
        return repository.get(resourceType());
    }
}