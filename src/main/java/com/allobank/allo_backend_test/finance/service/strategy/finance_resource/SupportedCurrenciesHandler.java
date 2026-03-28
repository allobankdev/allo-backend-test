package com.allobank.allo_backend_test.finance.service.strategy.finance_resource;

import com.allobank.allo_backend_test.finance.client.DataSourceClient;
import com.allobank.allo_backend_test.finance.model.CurrenciesModel;
import com.allobank.allo_backend_test.finance.model.FinanceResource;
import com.allobank.allo_backend_test.finance.model.dto.CurrenciesDto;
import com.allobank.allo_backend_test.finance.repository.FinanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SupportedCurrenciesHandler extends AbstractFinanceResourceHandler {

    @Autowired private DataSourceClient client;

    public SupportedCurrenciesHandler(FinanceRepository repository) {
        super(repository);
    }

    @Override
    public String resourceType() {
        return "supported_currencies";
    }

    @Override
    public FinanceResource fetch() {
        CurrenciesDto dto = client.get("/currencies", CurrenciesDto.class);
        CurrenciesModel model = new CurrenciesModel(dto);
        repository.put(resourceType(), model);
        return model;
    }
}