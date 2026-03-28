package com.allobank.allo_backend_test.finance.service.strategy.finance_resource;

import com.allobank.allo_backend_test.finance.client.DataSourceClient;
import com.allobank.allo_backend_test.finance.model.FinanceResource;
import com.allobank.allo_backend_test.finance.model.HistoricalRatesModel;
import com.allobank.allo_backend_test.finance.model.dto.HistoricalRatesDto;
import com.allobank.allo_backend_test.finance.repository.FinanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class HistoricalIdrUsdHandler extends AbstractFinanceResourceHandler {

    private final DataSourceClient client;

    public HistoricalIdrUsdHandler(FinanceRepository repository, DataSourceClient client) {
        super(repository);
        this.client = client;
    }

    @Override
    public String resourceType() {
        return "historical_idr_usd";
    }

    @Override
    public FinanceResource fetch() {
        HistoricalRatesDto dto = client.getWithParams(
                "/2024-01-01..2024-01-05",
                Map.of("from", "IDR", "to", "USD"),
                HistoricalRatesDto.class);

        HistoricalRatesModel model = new HistoricalRatesModel(
                dto.amount(), dto.base(), dto.startDate(), dto.endDate(), dto.rates());
        repository.put(resourceType(), model);
        return model;
    }
}