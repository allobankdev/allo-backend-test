package test.allo.backend;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import test.allo.backend.client.FrankFurtherApiClient;
import test.allo.backend.storage.InMemoryStorage;

import static test.allo.backend.utils.ConstantUtils.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartupLoader implements ApplicationRunner {

    private final FrankFurtherApiClient client;
    private final InMemoryStorage storage;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        JsonNode latestIdrRates = client.fetchLatestIdrRates();
        if (latestIdrRates != null) storage.save(LATEST_IDR_RATE, latestIdrRates);

        JsonNode historicalIdrUsd = client.fetchHistoricalIdrUsd();
        if (historicalIdrUsd != null) storage.save(HISTORICAL_IDR_USD, historicalIdrUsd);

        JsonNode supportedCurrencies = client.fetchSupportedCurrencies();
        if(supportedCurrencies != null) storage.save(SUPPORTED_CURRENCIES, supportedCurrencies);

        storage.lockStorage();

    }
}
