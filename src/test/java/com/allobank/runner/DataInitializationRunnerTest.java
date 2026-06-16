package com.allobank.runner;

import com.allobank.service.InMemoryDataStore;
import com.allobank.strategy.IDRDataFetcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataInitializationRunnerTest {

    @Mock
    private IDRDataFetcher fetcher1;

    @Mock
    private IDRDataFetcher fetcher2;

    @Mock
    private IDRDataFetcher fetcher3;

    private InMemoryDataStore dataStore;
    private DataInitializationRunner runner;

    @BeforeEach
    void setUp() {
        dataStore = new InMemoryDataStore();
        List<IDRDataFetcher> fetchers = Arrays.asList(fetcher1, fetcher2, fetcher3);
        runner = new DataInitializationRunner(fetchers, dataStore);

        when(fetcher1.getResourceType()).thenReturn("resource1");
        when(fetcher2.getResourceType()).thenReturn("resource2");
        when(fetcher3.getResourceType()).thenReturn("resource3");
    }

    @Test
    void testRun_Success() throws InterruptedException {
        // Arrange
        when(fetcher1.fetchData()).thenReturn(Mono.just("data1"));
        when(fetcher2.fetchData()).thenReturn(Mono.just("data2"));
        when(fetcher3.fetchData()).thenReturn(Mono.just("data3"));

        // Act
        runner.run(null);

        // Wait a bit for async operations
        Thread.sleep(1000);

        // Assert
        verify(fetcher1, times(1)).fetchData();
        verify(fetcher2, times(1)).fetchData();
        verify(fetcher3, times(1)).fetchData();
        assertTrue(dataStore.isDataLoaded());
    }

    @Test
    void testRun_WithErrors() throws InterruptedException {
        // Arrange
        when(fetcher1.fetchData()).thenReturn(Mono.just("data1"));
        when(fetcher2.fetchData()).thenReturn(Mono.error(new RuntimeException("Error")));
        when(fetcher3.fetchData()).thenReturn(Mono.just("data3"));

        // Act
        runner.run(null);

        // Wait a bit for async operations
        Thread.sleep(1000);

        // Assert
        verify(fetcher1, times(1)).fetchData();
        verify(fetcher2, times(1)).fetchData();
        verify(fetcher3, times(1)).fetchData();
        assertTrue(dataStore.isDataLoaded());
    }
}

