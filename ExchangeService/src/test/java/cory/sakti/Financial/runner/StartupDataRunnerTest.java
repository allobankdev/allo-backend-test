package cory.sakti.Financial.runner;

import cory.sakti.Financial.service.InMemoryDataStoreService;
import cory.sakti.Financial.strategy.FinancialDataStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StartupDataRunnerTest {
    @Mock private FinancialDataStrategy mockStrategy;
    @Mock private InMemoryDataStoreService dataStore;
    @Mock private RestTemplate restTemplate;
    @Mock private org.springframework.boot.ApplicationArguments mockArgs; // Mock the args

    private StartupDataRunner runner;

    @BeforeEach
    void setUp() {
        runner = new StartupDataRunner(List.of(mockStrategy), dataStore, restTemplate);
    }

    @Test
    @DisplayName("Runner should execute strategies and initialize store in correct order")
    void shouldExecuteStrategiesAndInitializeStore() throws Exception {
        // 1. Arrange
        String resourceKey = "test_resource";
        Object mockData = new Object();

        when(mockStrategy.getResourceType()).thenReturn(resourceKey);
        when(mockStrategy.fetchAndTransform(restTemplate)).thenReturn(mockData);

        // 2. Act
        runner.run(mockArgs);

        // 3. Assert: Verify order to satisfy Constraint C (Locking)
        org.mockito.InOrder inOrder = org.mockito.Mockito.inOrder(dataStore);
        inOrder.verify(dataStore).put(resourceKey, mockData);
        inOrder.verify(dataStore).markInitialized();
    }
}
