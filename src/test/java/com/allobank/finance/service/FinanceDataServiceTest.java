package com.allobank.finance.service;

import com.allobank.finance.exception.ServiceException;
import com.allobank.finance.model.response.FinanceDataResponseBuilder;
import com.allobank.finance.util.Constants;
import com.allobank.finance.util.ErrorCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class FinanceDataServiceTest {

    @InjectMocks
    FinanceDataService service;

    @Mock
    FinanceDataStore store;

    @Test
    void shouldReturnFinanceDataResponseWhenResourceTypeExists() {
        String resourceType = Constants.LATEST_IDR_RATES;

        var mockResponse = FinanceDataResponseBuilder.builder()
                        .resourceType(resourceType)
                        .fetchDate(ZonedDateTime.now())
                        .data(List.of())
                        .build();

        Mockito.when(store.get(resourceType)).thenReturn(mockResponse);

        var result = service.get(resourceType);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(resourceType, result.resourceType());
        Mockito.verify(store).get(resourceType);
    }

    @Test
    void shouldThrowExceptionWhenResourceTypeNotFound() {
        String resourceType = "invalid";

        Mockito.when(store.get(resourceType)).thenReturn(null);

        var exception = assertThrows(ServiceException.class,
                        () -> service.get(resourceType));

        Assertions.assertEquals(ErrorCode.INVALID_INPUT.getMessage(), exception.getMessage());
        Mockito.verify(store).get(resourceType);
    }
}
