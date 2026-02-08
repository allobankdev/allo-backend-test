package com.allobank.test.service;

import com.allobank.test.repository.FinanceDataRepository;
import com.allobank.test.strategy.IdrDataFetcher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FinanceServiceTest {

    @Mock
    private FinanceDataRepository repository;
    @Mock
    private IdrDataFetcher strategy;

    private FinanceService service;

    @BeforeEach
    void setUp() {
        lenient().when(strategy.getResourceType()).thenReturn("mock_resource");
        CompletableFuture<Object> mockFuture = CompletableFuture.completedFuture("Mock Data");
        lenient().doReturn(mockFuture).when(strategy).fetchData();

        List<IdrDataFetcher> strategies = Collections.singletonList(strategy);
        service = new FinanceService(strategies, repository);
    }

    @Test
    void testFetchAndCacheAllData_Success() {
        service.fetchAndCacheAllData().join();
        verify(repository, times(1)).saveData(eq("mock_resource"), any());
    }

    @Test
    void testGetCachedData_Success() {
        when(repository.getData("mock_resource")).thenReturn("Cached Data");
        Object data = service.getCachedData("mock_resource");
        Assertions.assertEquals("Cached Data", data);
    }

    @Test
    void testGetCachedData_InvalidType_ThrowsException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            service.getCachedData("ngawur_type");
        });
    }

    @Test
    void testGetCachedData_NotInitialized_ThrowsException() {
        when(repository.getData("mock_resource")).thenReturn(null);

        Assertions.assertThrows(IllegalStateException.class, () -> {
            service.getCachedData("mock_resource");
        });
    }
}