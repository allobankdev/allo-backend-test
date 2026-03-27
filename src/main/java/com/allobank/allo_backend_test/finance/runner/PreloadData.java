package com.allobank.allo_backend_test.finance.runner;

import com.allobank.allo_backend_test.finance.client.DataSourceClient;
import com.allobank.allo_backend_test.finance.model.CurrenciesModel;
import com.allobank.allo_backend_test.finance.model.HistoricalRatesModel;
import com.allobank.allo_backend_test.finance.model.LatestRatesModel;
import com.allobank.allo_backend_test.finance.repository.FinanceRepository;
import com.allobank.allo_backend_test.finance.service.SpreadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PreloadData implements ApplicationRunner {

    private final DataSourceClient client;
    private final FinanceRepository repository;
    private final SpreadService spreadService;

    @Override
    public void run(ApplicationArguments args) {
        fetchLatestRates();
        fetchHistoricalRates();
        fetchCurrencies();
        log.info("Data Repository: '{}'", repository.getData());
    }

    private void fetchLatestRates() {
        var dto = client.getLatestRates("IDR");
        Double spread = spreadService.calculateSpread(dto.rates().get("USD"));
        LatestRatesModel model = new LatestRatesModel(
                dto.amount(), dto.base(), dto.date(), dto.rates(), spread);
        repository.put(model.resourceType(), model);
        log.info("preload {}", model.resourceType());
    }

    private void fetchHistoricalRates() {
        var dto = client.getHistoricalRates("2024-01-01", "2024-01-05", "IDR", "USD");
        HistoricalRatesModel model = new HistoricalRatesModel(
                dto.amount(), dto.base(), dto.startDate(), dto.endDate(), dto.rates());
        repository.put(model.resourceType(), model);
        log.info("preload {}", model.resourceType());
    }

    private void fetchCurrencies() {
        var dto = client.getCurrencies();
        CurrenciesModel model = new CurrenciesModel(dto);
        repository.put(model.resourceType(), model);
        log.info("preload {}", model.resourceType());
    }
}