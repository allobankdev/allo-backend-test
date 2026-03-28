package com.allobank.allo_backend_test.finance.service.strategy.finance_resource;

import com.allobank.allo_backend_test.finance.client.DataSourceClient;
import com.allobank.allo_backend_test.finance.model.FinanceResource;
import com.allobank.allo_backend_test.finance.model.LatestRatesModel;
import com.allobank.allo_backend_test.finance.model.dto.LatestRateDto;
import com.allobank.allo_backend_test.finance.repository.FinanceRepository;
import com.allobank.allo_backend_test.finance.service.SpreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class LatestIdrRatesHandler extends AbstractFinanceResourceHandler {

    private final DataSourceClient client;
    private final SpreadService spreadService;

    public LatestIdrRatesHandler(FinanceRepository repository, DataSourceClient client, SpreadService spreadService) {
        super(repository);
        this.client = client;
        this.spreadService = spreadService;
    }

    @Override
    public String resourceType() {
        return "latest_idr_rates";
    }

    @Override
    public FinanceResource fetch() {
        LatestRateDto dto = client.getWithParams("/latest", Map.of("base", "IDR"), LatestRateDto.class);
        Double spread = spreadService.calculateSpread(dto.rates().get("USD"));
        LatestRatesModel model = new LatestRatesModel(
                dto.amount(), dto.base(), dto.date(), dto.rates(), spread);
        repository.put(resourceType(), model);
        return model;
    }
}