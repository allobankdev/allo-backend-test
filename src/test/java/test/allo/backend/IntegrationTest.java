package test.allo.backend;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import test.allo.backend.client.FrankFurtherApiClient;
import test.allo.backend.storage.InMemoryStorage;

import static org.junit.jupiter.api.AssertionsKt.assertNotNull;
import static org.mockito.Mockito.when;
import static test.allo.backend.utils.ConstantUtils.*;

@SpringBootTest(properties = "spring.profiles.active=test")
public class IntegrationTest {

    @Autowired
    private InMemoryStorage storage;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private FrankFurtherApiClient client;

    @Test
    void testStartupLoader() throws Exception {
        JsonNode latestIdrRates = mapper.readTree(new ClassPathResource("/test/latest-idr-rates.json").getFile());
        JsonNode historicalIdrUsd = mapper.readTree(new ClassPathResource("/test/historical-idr-usd.json").getFile());
        JsonNode supportedCurrencies = mapper.readTree(new ClassPathResource("/test/supported-currencies.json").getFile());

        when(client.fetchLatestIdrRates()).thenReturn(latestIdrRates);
        when(client.fetchHistoricalIdrUsd()).thenReturn(historicalIdrUsd);
        when(client.fetchSupportedCurrencies()).thenReturn(supportedCurrencies);

        new StartupLoader(client, storage).run(null);
        assertNotNull(storage.get(LATEST_IDR_RATE));
        assertNotNull(storage.get(HISTORICAL_IDR_USD));
        assertNotNull(storage.get(SUPPORTED_CURRENCIES));

    }
}
