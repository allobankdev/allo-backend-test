package id.co.microservice.currency.currency_service.service.impl;

import id.co.microservice.currency.currency_service.dto.CurrencyResponseDto;
import id.co.microservice.currency.currency_service.exception.CurrencyException;
import id.co.microservice.currency.currency_service.service.CurrencyStrategy;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CurrencyServiceImplTest {

    @Test
    void testExecuteStrategy_ReturnsResponseDto() {
        CurrencyStrategy mockStrategy = mock(CurrencyStrategy.class);
        CurrencyResponseDto mockResponse = new CurrencyResponseDto();
        mockResponse.setBase("IDR");
        mockResponse.setDate("2024-01-05");

        when(mockStrategy.execute()).thenReturn(mockResponse);

        Map<String, CurrencyStrategy> strategies = new HashMap<>();
        strategies.put("latest_idr_usd", mockStrategy);

        CurrencyServiceImpl service = new CurrencyServiceImpl(strategies);

        CurrencyResponseDto result = service.executeStrategy("latest_idr_usd");

        assertNotNull(result);
        assertEquals("IDR", result.getBase());
        assertEquals("2024-01-05", result.getDate());
        verify(mockStrategy, times(1)).execute();
    }

    @Test
    void testExecuteStrategy_ThrowsCurrencyExceptionForUnsupportedType() {
        Map<String, CurrencyStrategy> strategies = new HashMap<>();
        CurrencyServiceImpl service = new CurrencyServiceImpl(strategies);

        CurrencyException ex = assertThrows(CurrencyException.class,
                () -> service.executeStrategy("unsupported_type"));

        assertEquals("Unsupported resource type: unsupported_type", ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus()); // default from CurrencyException
    }
}
