package com.allobank.finnance.allobankfinance.service;

import com.allobank.finnance.allobankfinance.constant.ResourceTypeConstant;
import com.allobank.finnance.allobankfinance.dto.FinanceRequestDto;
import com.allobank.finnance.allobankfinance.integration.FrankfurterIntegrationService;
import com.allobank.finnance.allobankfinance.service.impl.SupportedCurrenciesServiceImpl;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class SupportedCurrenciesServiceImplTest {

    @Mock
    private FrankfurterIntegrationService frankfurterService;

    private SupportedCurrenciesServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new SupportedCurrenciesServiceImpl(frankfurterService);
    }

    @Test
    void shouldReturnSupportedCurrencies() {
        // given
        Map<String, String> mockCurrencies = Map.of(
                "USD", "United States Dollar",
                "IDR", "Indonesian Rupiah",
                "EUR", "Euro"
        );

        Mockito.when(frankfurterService.getSupportedCurrencies())
                .thenReturn(mockCurrencies);

        // when
        Object result = service.fetchData(new FinanceRequestDto());

        // then
        AssertionsForClassTypes.assertThat(result)
                .isInstanceOf(Map.class)
                .isEqualTo(mockCurrencies);

        Mockito.verify(frankfurterService, Mockito.times(1))
                .getSupportedCurrencies();
    }

    @Test
    void shouldReturnCorrectResourceType() {
        // when
        String resourceType = service.getResourceType();

        // then
        AssertionsForClassTypes.assertThat(resourceType)
                .isEqualTo(ResourceTypeConstant.SUPPORTED_CURRENCIES);
    }
}
