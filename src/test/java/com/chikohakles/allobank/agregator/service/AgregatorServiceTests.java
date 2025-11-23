package com.chikohakles.allobank.agregator.service;

import com.chikohakles.allobank.agregator.constant.ResourceType;
import com.chikohakles.allobank.agregator.dto.DateQueryResponse;
import com.chikohakles.allobank.agregator.dto.LatestResponse;
import com.chikohakles.allobank.agregator.service.AgregatorService;
import com.chikohakles.allobank.agregator.service.impl.AgregatorServiceImpl;
import com.chikohakles.allobank.agregator.store.AgregatorDataStore;
import com.chikohakles.allobank.agregator.validator.ResourceTypeValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgregatorServiceTests {

    @Mock
    ResourceTypeValidator resourceTypeValidator;

    @Mock
    AgregatorDataStore agregatorDataStore;

    @InjectMocks
    AgregatorServiceImpl agregatorService;

    @Test
    void getData_ShouldValidateAndDelegateToDataStore_LATEST_IDR_RATES() {
        String resourceTypeCode = "latest_idr_rates";
        ResourceType expectedResourceType = ResourceType.LATEST_IDR_RATES;
        LatestResponse expectedData = new LatestResponse();

        doNothing().when(resourceTypeValidator).validate("LATEST_IDR_RATES");

        when(agregatorDataStore.get(expectedResourceType)).thenReturn(expectedData);

        LatestResponse result = (LatestResponse) agregatorService.getData(resourceTypeCode);

        assertThat(result).isEqualTo(expectedData);
        verify(resourceTypeValidator).validate("LATEST_IDR_RATES");
        verify(agregatorDataStore).get(expectedResourceType);
    }

    @Test
    void getData_ShouldValidateAndDelegate_HISTORICAL_IDR_USD() {
        String resourceTypeCode = "historical_idr_usd";
        ResourceType expectedResourceType = ResourceType.HISTORICAL_IDR_USD;
        DateQueryResponse expectedData = new DateQueryResponse();

        doNothing().when(resourceTypeValidator).validate("HISTORICAL_IDR_USD");
        when(agregatorDataStore.get(expectedResourceType)).thenReturn(expectedData);

        DateQueryResponse result = (DateQueryResponse) agregatorService.getData(resourceTypeCode);

        assertThat(result).isEqualTo(expectedData);
        verify(resourceTypeValidator).validate("HISTORICAL_IDR_USD");
        verify(agregatorDataStore).get(expectedResourceType);
    }

    @Test
    void getData_ShouldValidateAndDelegate_SUPPORTED_CURRENCIES() {
        String resourceTypeCode = "supported_currencies";
        ResourceType expectedResourceType = ResourceType.SUPPORTED_CURRENCIES;
        Map expectedData = new HashMap();

        doNothing().when(resourceTypeValidator).validate("SUPPORTED_CURRENCIES");
        when(agregatorDataStore.get(expectedResourceType)).thenReturn(expectedData);

        Map result = (Map) agregatorService.getData(resourceTypeCode);

        assertThat(result).isEqualTo(expectedData);
        verify(resourceTypeValidator).validate("SUPPORTED_CURRENCIES");
        verify(agregatorDataStore).get(expectedResourceType);
    }

    @Test
    void getData_WhenValidatorThrows_ShouldPropagateException() {
        String invalid = "invalid_type";
        doThrow(new IllegalArgumentException("invalid_type is not a valid resource type."))
                .when(resourceTypeValidator).validate("INVALID_TYPE");

        assertThrows(IllegalArgumentException.class,
                () -> agregatorService.getData(invalid));

        verifyNoInteractions(agregatorDataStore);
    }
}
