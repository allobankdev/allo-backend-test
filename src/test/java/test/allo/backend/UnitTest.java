package test.allo.backend;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.util.ReflectionTestUtils;
import test.allo.backend.service.impl.HistoricalIdrUsdImpl;
import test.allo.backend.service.impl.LatestIdrRatesImpl;
import test.allo.backend.service.impl.SupportedCurrenciesImpl;
import test.allo.backend.storage.InMemoryStorage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static test.allo.backend.utils.ConstantUtils.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class UnitTest {

    @Mock
    private InMemoryStorage storage;

    @InjectMocks
    private LatestIdrRatesImpl latestIdrRates;

    @InjectMocks
    private HistoricalIdrUsdImpl historicalIdrUsd;

    @InjectMocks
    private SupportedCurrenciesImpl supportedCurrencies;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void testLatestIdrRates() throws Exception {
        JsonNode input = mapper.readTree(new ClassPathResource("/test/latest-idr-rates.json").getFile());
        JsonNode expected = mapper.readTree(new ClassPathResource("/test/latest-idr-rates-transformed.json").getFile());
        when(storage.get(LATEST_IDR_RATE)).thenReturn(input);

        ReflectionTestUtils.setField(latestIdrRates, "mapper", mapper);
        ReflectionTestUtils.setField(latestIdrRates, "username", "vilis-iv");
        JsonNode result = latestIdrRates.fetchData();

        assertEquals(expected, result);
    }

    @Test
    void testHistoricalIdrUsd() throws Exception {
        JsonNode input = mapper.readTree(new ClassPathResource("/test/historical-idr-usd.json").getFile());
        JsonNode expected = mapper.readTree(new ClassPathResource("/test/historical-idr-usd-transformed.json").getFile());
        when(storage.get(HISTORICAL_IDR_USD)).thenReturn(input);

        ReflectionTestUtils.setField(historicalIdrUsd, "mapper", mapper);
        JsonNode result = historicalIdrUsd.fetchData();

        assertEquals(expected, result);
    }

    @Test
    void testSupportedCurrencies() throws Exception {
        JsonNode input = mapper.readTree(new ClassPathResource("/test/supported-currencies.json").getFile());
        JsonNode expected = mapper.readTree(new ClassPathResource("/test/supported-currencies-transformed.json").getFile());
        when(storage.get(SUPPORTED_CURRENCIES)).thenReturn(input);

        ReflectionTestUtils.setField(supportedCurrencies, "mapper", mapper);
        JsonNode result = supportedCurrencies.fetchData();

        assertEquals(expected, result);
    }
}
