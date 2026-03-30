package com.allobank.test.runner;

import com.allobank.test.service.DataStoreService;
import com.allobank.test.strategy.DataFetcherStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.ApplicationArguments;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InitialDataRunnerTest {

    @Mock
    private DataFetcherStrategy mockStrategy;

    @Mock
    private DataStoreService dataStoreService;

    @Test
    void testApplicationRunnerStoresDataFromStrategy() {
        when(mockStrategy.getResourceType()).thenReturn("test_resource");
        when(mockStrategy.fetchAndTransform()).thenReturn("MOCK_PAYLOAD");

        List<DataFetcherStrategy> strategies = Collections.singletonList(mockStrategy);
        
        InitialDataRunner runner = new InitialDataRunner(strategies, dataStoreService);
        
        ApplicationArguments mockArgs = mock(ApplicationArguments.class);
        runner.run(mockArgs);

        verify(mockStrategy, times(1)).fetchAndTransform();
        verify(dataStoreService, times(1)).storeData("test_resource", "MOCK_PAYLOAD");
    }
}
