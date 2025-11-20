package com.allo.idr;

import com.allo.idr.cache.ImmutableDataCache;
import com.allo.idr.enums.ResourceType;
import com.allo.idr.model.LatestRateResponse;
import com.allo.idr.runner.InitialDataLoader;
import com.allo.idr.service.FetcherStrategyRegistry;
import com.allo.idr.service.LatestRatesFetcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class IDRDataLoaderIntegrationTest {

    @MockBean
    LatestRatesFetcher latestRatesFetcher;

    @MockBean
    FetcherStrategyRegistry regis;

    @Autowired
    InitialDataLoader loader;

    @Autowired
    ImmutableDataCache cache;

    @BeforeEach
    void setup(){
        cache.reset();
        Mockito.when(regis.getAllResourcesType())
                .thenReturn(Set.of(ResourceType.LATEST_IDR_RATES));
        Mockito.when(regis.getStrategy(ResourceType.LATEST_IDR_RATES))
                .thenReturn(Optional.of(latestRatesFetcher));
    }

    @Test
    void testApplicationRunner() throws Exception {
        LatestRateResponse res = new LatestRateResponse();
        res.setBase("IDR");
        res.setDate("2024-01-01");
        res.setRates(Map.of("USD", 15000.0));
        res.setUsdBuySpreadIdr(0.000066);

        Mockito.when(latestRatesFetcher.fetcData())
                .thenReturn(Collections.singletonList(res));

        loader.run(null);
        List<?> cek = cache.get(ResourceType.LATEST_IDR_RATES);
        assertNotNull(cek);
        assertFalse(cek.isEmpty());
        assertEquals(res, cek.get(0));
    }
}
