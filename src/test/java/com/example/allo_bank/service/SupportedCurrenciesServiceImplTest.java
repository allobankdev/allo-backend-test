package com.example.allo_bank.service;

import com.example.allo_bank.dto.ApiResponse;
import com.example.allo_bank.service.impl.SupportedCurrenciesServiceImpl;
import com.example.allo_bank.util.Cache;
import com.example.allo_bank.util.TypeEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.HashMap;
import java.util.Map;

import static com.example.allo_bank.util.Constant.SUCCESS;
import static com.example.allo_bank.util.Constant.USD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class SupportedCurrenciesServiceImplTest {

    @Autowired
    private SupportedCurrenciesServiceImpl service;

    @MockitoBean
    private Cache cache;

    @Test
    void shouldReturnSupportedCurrenciesFromCache() {

        Map<String, String> mockMap = new HashMap<>();
        mockMap.put(USD, "United States Dollar");
        mockMap.put("EUR", "Euro");

        when(cache.getDataCache(TypeEnum.supported_currencies)).thenReturn(mockMap);

        ApiResponse<Object> result = service.fetchData();

        verify(cache).getDataCache(TypeEnum.supported_currencies);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(SUCCESS);
        assertThat(result.getResourceType()).isEqualTo("supported_currencies");
        assertThat(result.getData()).isInstanceOf(Map.class);

        Map<String, String> responseMap = (Map<String, String>) result.getData();

        assertThat(responseMap).hasSize(2);
        assertThat(responseMap).containsEntry(USD, "United States Dollar");
        assertThat(responseMap).containsEntry("EUR", "Euro");
    }

}

