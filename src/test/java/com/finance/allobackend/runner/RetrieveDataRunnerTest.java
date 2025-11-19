package com.finance.allobackend.runner;

import com.finance.allobackend.strategy.FinanceStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.ApplicationArguments;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RetrieveDataRunnerTest {
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ApplicationArguments applicationArguments;

    @Mock
    private FinanceStrategy strategyFirst;

    @Mock
    private FinanceStrategy strategySecond;

    private RetrieveDataRunner runner;

    @BeforeEach
    void setUp() {
        List<FinanceStrategy> strategies = Arrays.asList(strategyFirst, strategySecond);

        when(strategyFirst.getResourceType()).thenReturn("strategyFirst");
        when(strategySecond.getResourceType()).thenReturn("strategySecond");

        runner = new RetrieveDataRunner(restTemplate, strategies);
    }

    @Test
    void run_TriggerAllStrategies() throws Exception {
        runner.run(applicationArguments);

        verify(strategyFirst, times(1)).getOrRefreshData(restTemplate);
        verify(strategySecond, times(1)).getOrRefreshData(restTemplate);
    }

    @Test
    void run_TriggerAllStrategiesError() throws Exception {
        doThrow(new RuntimeException("Connection Timeout to API")).when(strategyFirst).getOrRefreshData(any());
        runner.run(applicationArguments);

        verify(strategyFirst, times(1)).getOrRefreshData(restTemplate);
        verify(strategySecond, times(1)).getOrRefreshData(restTemplate);
    }
}
