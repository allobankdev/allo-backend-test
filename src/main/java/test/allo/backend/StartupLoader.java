package test.allo.backend;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import test.allo.backend.client.FrankFurtherApiClient;
import test.allo.backend.storage.InMemoryStorage;

import static test.allo.backend.utils.ConstantUtils.*;
import static test.allo.backend.utils.StorageUtils.isValidData;

@Slf4j
@Component
@Profile("!test")
@RequiredArgsConstructor
public class StartupLoader implements ApplicationRunner {

    private final FrankFurtherApiClient client;
    private final InMemoryStorage storage;

    @Override
    public void run(ApplicationArguments args) {
        JsonNode latestIdrRates = client.fetchLatestIdrRates();
        saveData(LATEST_IDR_RATE, latestIdrRates);

        JsonNode historicalIdrUsd = client.fetchHistoricalIdrUsd();
        saveData(HISTORICAL_IDR_USD, historicalIdrUsd);

        JsonNode supportedCurrencies = client.fetchSupportedCurrencies();
        saveData(SUPPORTED_CURRENCIES, supportedCurrencies);
    }

    private void saveData(String key, JsonNode data) {
        storage.save(key, data, isValidData(data));
    }

}
