package com.musfiul.idrrateaggregator.runner;

import com.musfiul.idrrateaggregator.constant.ResourceType;
import com.musfiul.idrrateaggregator.dto.LatestRatesResponse;
import com.musfiul.idrrateaggregator.dto.api.APIResponseDTO;
import com.musfiul.idrrateaggregator.service.resolver.FetchResolver;
import com.musfiul.idrrateaggregator.service.data.FinanceDataStoreService;
import com.musfiul.idrrateaggregator.service.fetcher.IDRDataFetcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class FinanceStartupRunnerIntegrationTest {

    @Autowired
    private FinanceDataStoreService store;

    @Autowired
    private TestFetcherHolder holder;

    @Test
    void shouldInitializeStoreOnStartup() {

        for (ResourceType type : ResourceType.values()) {
            APIResponseDTO data = store.get(type);

            assertNotNull(data, "Store should contain data for type: " + type);
            assertInstanceOf(LatestRatesResponse.class, data.getData());
        }

        verify(holder.getFetcher(), atLeast(ResourceType.values().length))
                .fetchData();
    }

    @TestConfiguration
    static class TestConfig {

        private final IDRDataFetcher fetcher = mock(IDRDataFetcher.class);

        @Bean
        public TestFetcherHolder testFetcherHolder() {
            return new TestFetcherHolder(fetcher);
        }

        @Bean
        @Primary
        public FetchResolver fetchResolver() {

            when(fetcher.fetchData()).thenReturn(new LatestRatesResponse());

            return type -> fetcher;
        }
    }

    static class TestFetcherHolder {
        private final IDRDataFetcher fetcher;

        TestFetcherHolder(IDRDataFetcher fetcher) {
            this.fetcher = fetcher;
        }

        public IDRDataFetcher getFetcher() {
            return fetcher;
        }
    }
}