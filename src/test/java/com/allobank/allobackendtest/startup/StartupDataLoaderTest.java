package com.allobank.allobackendtest.startup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.allobank.allobackendtest.store.InMemoryDataStore;
import com.allobank.allobackendtest.strategy.IDRDataFetcher;

@SpringBootTest
class StartupDataLoaderTest {

    @Mock
    private IDRDataFetcher fetcher1;

    @Mock
    private IDRDataFetcher fetcher2;

    @Mock
    private IDRDataFetcher fetcher3;

    private InMemoryDataStore dataStore;
    private StartupDataLoader loader;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        dataStore = new InMemoryDataStore();

        when(fetcher1.getResourceType()).thenReturn("latest_idr_rates");
        when(fetcher2.getResourceType()).thenReturn("historical_idr_usd");
        when(fetcher3.getResourceType()).thenReturn("supported_currencies");

        loader = new StartupDataLoader(
                List.of(fetcher1, fetcher2, fetcher3),
                dataStore
        );
    }

    @Test
    void shouldLoadAllFetcherDataOnStartup() throws Exception {

        when(fetcher1.fetchData()).thenReturn("latest-data");
        when(fetcher2.fetchData()).thenReturn("historical-data");
        when(fetcher3.fetchData()).thenReturn("supported-currencies");

        loader.run();

        assertThat(dataStore.get("latest_idr_rates")).isEqualTo("latest-data");
        assertThat(dataStore.get("historical_idr_usd")).isEqualTo("historical-data");
        assertThat(dataStore.get("supported_currencies")).isEqualTo("supported-currencies");
    }

    @Test
    void shouldContinueLoadingEvenIfOneFetcherFails() throws Exception {

        when(fetcher1.fetchData()).thenThrow(new RuntimeException("API down"));
        when(fetcher2.fetchData()).thenReturn("historical-data");
        when(fetcher3.fetchData()).thenReturn("supported-currencies");

        loader.run();

        assertThat(dataStore.containsKey("latest_idr_rates")).isFalse();
        assertThat(dataStore.get("historical_idr_usd")).isEqualTo("historical-data");
        assertThat(dataStore.get("supported_currencies")).isEqualTo("supported-currencies");
    }

}
