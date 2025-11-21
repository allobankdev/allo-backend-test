package com.example.allo_bank.service;

import com.example.allo_bank.dto.ApiResponse;
import com.example.allo_bank.dto.LatestIdrRatesResponse;
import com.example.allo_bank.integration.dto.LatestIdrRatesDto;
import com.example.allo_bank.service.impl.LatestGetStorageRatesServiceImpl;
import com.example.allo_bank.util.Cache;
import com.example.allo_bank.util.Calculation;
import com.example.allo_bank.util.TypeEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.example.allo_bank.util.Constant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class LatestGetStorageRatesServiceImplTest {

    @Autowired
    private LatestGetStorageRatesServiceImpl service;

    @MockitoBean
    private Cache cache;

    @MockitoBean
    private Calculation calculation;

    @Test
    void shouldReturnLatestRatesResponseFromCache() {

        LatestIdrRatesDto mockDto = new LatestIdrRatesDto();
        mockDto.setBase(IDR);
        mockDto.setDate("2024-01-01");

        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put(USD, new BigDecimal("15750.00"));
        rates.put("EUR", new BigDecimal("17000.00"));
        mockDto.setRates(rates);

        when(cache.getDataCache(TypeEnum.latest_idr_rates)).thenReturn(mockDto);

        when(calculation.usdBuySpreadIdr(new BigDecimal("15750.00")))
                .thenReturn(new BigDecimal("0.00006"));

        ApiResponse<Object> result = service.fetchData();

        verify(cache).getDataCache(TypeEnum.latest_idr_rates);
        verify(calculation).usdBuySpreadIdr(new BigDecimal("15750.00"));

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(SUCCESS);
        assertThat(result.getResourceType()).isEqualTo("latest_idr_rates");

        assertThat(result.getData()).isInstanceOf(LatestIdrRatesResponse.class);

        LatestIdrRatesResponse response = (LatestIdrRatesResponse) result.getData();

        assertThat(response.getBase()).isEqualTo(IDR);
        assertThat(response.getDate()).isEqualTo("2024-01-01");

        assertThat(response.getRates()).containsOnlyKeys(USD);
        assertThat(response.getRates().get(USD)).isEqualByComparingTo("15750.00");

        assertThat(response.getUsdBuySpreadIdr()).isEqualByComparingTo("0.00006");
    }
}
