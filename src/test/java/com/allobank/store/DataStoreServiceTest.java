package com.allobank.store;

import com.allobank.enums.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class DataStoreServiceTest {

    private DataStoreService dataStoreService;

    @BeforeEach
    void setUp() {
        dataStoreService = new DataStoreService();
    }


    @Test
    void testIsInitialized_FalseAtStart() {
        assertThat(dataStoreService.isInitialized()).isFalse();
    }

    @Test
    void testMarkInitialized_FreezesStoreAndClearsMutable() {
        // Arrange
        dataStoreService.storeData(ResourceType.HISTORICAL_IDR_USD, "AAA");

        // Act
        dataStoreService.markInitialized();

        // Assert
        assertThat(dataStoreService.isInitialized()).isTrue();
        assertThat(dataStoreService.getData(ResourceType.HISTORICAL_IDR_USD.getValue()))
                .isEqualTo("AAA");

        // Mutability blocked
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> dataStoreService.getAllData().put("x", "y"));
    }

    @Test
    void testMarkInitialized_SecondCallDoesNothing() {
        // Arrange
        dataStoreService.storeData(ResourceType.LATEST_IDR_RATES, "123");

        dataStoreService.markInitialized();
        Map<String, Object> first = dataStoreService.getAllData();

        // Act
        dataStoreService.markInitialized();  // Does nothing

        // Assert
        Map<String, Object> second = dataStoreService.getAllData();
        assertThat(second).isSameAs(first);
    }


    @Test
    void testStoreData_BeforeInitialization() {
        // Arrange
        dataStoreService.storeData(ResourceType.SUPPORTED_CURRENCIES, "LIST");
        dataStoreService.markInitialized();

        // Act
        Object data = dataStoreService.getData(ResourceType.SUPPORTED_CURRENCIES.getValue());

        // Assert
        assertThat(data).isEqualTo("LIST");
    }

    @Test
    void testStoreData_AfterInitialization_Throws() {
        dataStoreService.markInitialized();
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() ->
                        dataStoreService.storeData(ResourceType.LATEST_IDR_RATES, "X")
                );
    }


    @Test
    void testGetData_NotInitialized_Throws() {
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() ->
                        dataStoreService.getData("something")
                );
    }

    @Test
    void testGetData_KeyExists() {
        // Arrange
        dataStoreService.storeData(ResourceType.LATEST_IDR_RATES, 999);
        dataStoreService.markInitialized();

        // Act
        Object result = dataStoreService.getData(ResourceType.LATEST_IDR_RATES.getValue());

        // Assert
        assertThat(result).isEqualTo(999);
    }

    @Test
    void testGetData_UnknownKey_Throws() {
        dataStoreService.markInitialized();
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() ->
                        dataStoreService.getData("not_exist")
                );
    }


    @Test
    void testGetAllData_NotInitialized_Throws() {
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() ->
                        dataStoreService.getAllData()
                );
    }

    @Test
    void testGetAllData_ReturnsImmutableMap() {
        // Arrange
        dataStoreService.storeData(ResourceType.HISTORICAL_IDR_USD, "DATA");
        dataStoreService.markInitialized();

        // Act
        Map<String, Object> all = dataStoreService.getAllData();

        // Assert
        assertThat(all)
                .containsEntry(ResourceType.HISTORICAL_IDR_USD.getValue(), "DATA");

        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> all.put("x", "y"));
    }


    @Test
    void testResourceType_FromValue_Valid() {
        assertThat(ResourceType.fromValue("latest_idr_rates"))
                .isEqualTo(ResourceType.LATEST_IDR_RATES);

        assertThat(ResourceType.fromValue("HISTORICAL_IDR_USD")) // case-insensitive
                .isEqualTo(ResourceType.HISTORICAL_IDR_USD);
    }

    @Test
    void testResourceType_FromValue_Invalid_Throws() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() ->
                        ResourceType.fromValue("invalid_key")
                );
    }
}