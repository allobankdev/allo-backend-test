package com.athallah.finance.strategy;

import com.athallah.finance.client.FinanceFrankfurterWebClient;
import com.athallah.finance.service.strategy.SupportedCurrenciesStrategy;
import com.athallah.finance.util.constant.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SupportedCurrenciesStrategyTest {

    @Mock
    private FinanceFrankfurterWebClient webClient;

    @InjectMocks
    private SupportedCurrenciesStrategy strategy;

    private Map<String, String> mockCurrencies;

    @BeforeEach
    void setUp() {
        mockCurrencies = new LinkedHashMap<>();
        mockCurrencies.put("AUD", "Australian Dollar");
        mockCurrencies.put("BGN", "Bulgarian Lev");
        mockCurrencies.put("BRL", "Brazilian Real");
        mockCurrencies.put("CAD", "Canadian Dollar");
        mockCurrencies.put("CHF", "Swiss Franc");
        mockCurrencies.put("USD", "United States Dollar");
        mockCurrencies.put("EUR", "Euro");
        mockCurrencies.put("GBP", "British Pound");
        mockCurrencies.put("IDR", "Indonesian Rupiah");
        mockCurrencies.put("JPY", "Japanese Yen");
    }

    @Test
    void fetchData_shouldCallWebClientAndReturnData() {
        // Given
        when(webClient.getSupportedCurrencies()).thenReturn(mockCurrencies);

        // When
        Object result = strategy.fetchData();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(Map.class);

        @SuppressWarnings("unchecked")
        Map<String, String> resultMap = (Map<String, String>) result;
        assertThat(resultMap).hasSize(10);
        assertThat(resultMap).containsEntry("USD", "United States Dollar");
        assertThat(resultMap).containsEntry("IDR", "Indonesian Rupiah");
        assertThat(resultMap).containsEntry("EUR", "Euro");

        verify(webClient, times(1)).getSupportedCurrencies();
    }

    @Test
    void getResourceType_shouldReturnSupportedCurrencies() {
        // When
        ResourceType result = strategy.getResourceType();

        // Then
        assertThat(result).isEqualTo(ResourceType.supported_currencies);
    }

    @Test
    void fetchData_shouldHandleEmptyResponse() {
        // Given
        when(webClient.getSupportedCurrencies()).thenReturn(new LinkedHashMap<>());

        // When
        Object result = strategy.fetchData();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(Map.class);

        @SuppressWarnings("unchecked")
        Map<String, String> resultMap = (Map<String, String>) result;
        assertThat(resultMap).isEmpty();
    }

    @Test
    void fetchData_shouldPreserveAllCurrencyData() {
        // Given
        when(webClient.getSupportedCurrencies()).thenReturn(mockCurrencies);

        // When
        Object result = strategy.fetchData();

        // Then
        @SuppressWarnings("unchecked")
        Map<String, String> resultMap = (Map<String, String>) result;
        assertThat(resultMap).containsKeys("AUD", "BGN", "BRL", "CAD", "CHF", "USD", "EUR", "GBP", "IDR", "JPY");
        assertThat(resultMap.get("CHF")).isEqualTo("Swiss Franc");
        assertThat(resultMap.get("BRL")).isEqualTo("Brazilian Real");
    }

    @Test
    void fetchData_shouldHandleFullCurrencyList() {
        // Given - Create full currency list like in actual response
        Map<String, String> fullCurrencies = new LinkedHashMap<>();
        fullCurrencies.put("AUD", "Australian Dollar");
        fullCurrencies.put("BGN", "Bulgarian Lev");
        fullCurrencies.put("BRL", "Brazilian Real");
        fullCurrencies.put("CAD", "Canadian Dollar");
        fullCurrencies.put("CHF", "Swiss Franc");
        fullCurrencies.put("CNY", "Chinese Renminbi Yuan");
        fullCurrencies.put("CZK", "Czech Koruna");
        fullCurrencies.put("DKK", "Danish Krone");
        fullCurrencies.put("EUR", "Euro");
        fullCurrencies.put("GBP", "British Pound");
        fullCurrencies.put("HKD", "Hong Kong Dollar");
        fullCurrencies.put("HUF", "Hungarian Forint");
        fullCurrencies.put("IDR", "Indonesian Rupiah");
        fullCurrencies.put("ILS", "Israeli New Sheqel");
        fullCurrencies.put("INR", "Indian Rupee");
        fullCurrencies.put("ISK", "Icelandic Króna");
        fullCurrencies.put("JPY", "Japanese Yen");
        fullCurrencies.put("KRW", "South Korean Won");
        fullCurrencies.put("MXN", "Mexican Peso");
        fullCurrencies.put("MYR", "Malaysian Ringgit");
        fullCurrencies.put("NOK", "Norwegian Krone");
        fullCurrencies.put("NZD", "New Zealand Dollar");
        fullCurrencies.put("PHP", "Philippine Peso");
        fullCurrencies.put("PLN", "Polish Złoty");
        fullCurrencies.put("RON", "Romanian Leu");
        fullCurrencies.put("SEK", "Swedish Krona");
        fullCurrencies.put("SGD", "Singapore Dollar");
        fullCurrencies.put("THB", "Thai Baht");
        fullCurrencies.put("TRY", "Turkish Lira");
        fullCurrencies.put("USD", "United States Dollar");
        fullCurrencies.put("ZAR", "South African Rand");

        when(webClient.getSupportedCurrencies()).thenReturn(fullCurrencies);

        // When
        Object result = strategy.fetchData();

        // Then
        @SuppressWarnings("unchecked")
        Map<String, String> resultMap = (Map<String, String>) result;
        assertThat(resultMap).hasSize(31);
        assertThat(resultMap).containsEntry("ISK", "Icelandic Króna");
        assertThat(resultMap).containsEntry("PLN", "Polish Złoty");
        assertThat(resultMap).containsEntry("ZAR", "South African Rand");
    }

    @Test
    void fetchData_shouldNotModifyOriginalData() {
        // Given
        when(webClient.getSupportedCurrencies()).thenReturn(mockCurrencies);

        // When
        Object result = strategy.fetchData();

        // Then
        assertThat(result).isEqualTo(mockCurrencies);
        verify(webClient, times(1)).getSupportedCurrencies();
        verifyNoMoreInteractions(webClient);
    }
}
