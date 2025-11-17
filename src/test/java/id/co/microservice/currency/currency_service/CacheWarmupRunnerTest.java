package id.co.microservice.currency.currency_service;

import id.co.microservice.currency.currency_service.dto.CurrencyResponseDto;
import id.co.microservice.currency.currency_service.service.CurrencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.ApplicationArguments;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CacheWarmupRunnerTest {

    @Mock
    private CurrencyService currencyService;

    @InjectMocks
    private CacheWarmupRunner cacheWarmupRunner;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRun_ExecutesStrategiesForAllResources() throws Exception {
        CurrencyResponseDto mockResponse = new CurrencyResponseDto();
        when(currencyService.executeStrategy(anyString())).thenReturn(mockResponse);

        ApplicationArguments args = mock(ApplicationArguments.class);

        cacheWarmupRunner.run(args);

        verify(currencyService, times(1)).executeStrategy("supported_currencies");
        verify(currencyService, times(1)).executeStrategy("latest_idr_usd");
        verify(currencyService, times(1)).executeStrategy("historical_idr_usd");
    }

    @Test
    void testRun_HandlesExceptionGracefully() throws Exception {
        when(currencyService.executeStrategy("supported_currencies"))
                .thenThrow(new RuntimeException("API error"));
        when(currencyService.executeStrategy("latest_idr_usd"))
                .thenReturn(new CurrencyResponseDto());
        when(currencyService.executeStrategy("historical_idr_usd"))
                .thenReturn(new CurrencyResponseDto());

        ApplicationArguments args = mock(ApplicationArguments.class);

        cacheWarmupRunner.run(args);

        verify(currencyService, times(1)).executeStrategy("supported_currencies");
        verify(currencyService, times(1)).executeStrategy("latest_idr_usd");
        verify(currencyService, times(1)).executeStrategy("historical_idr_usd");
    }

}