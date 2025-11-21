package com.example.allo_bank.service;

import com.example.allo_bank.dto.ApiResponse;
import com.example.allo_bank.dto.HistoricalIdrUsdResponse;
import com.example.allo_bank.integration.dto.HistoricalIdrUsdDto;
import com.example.allo_bank.service.impl.HistoricalGetStorageUsdServiceImpl;
import com.example.allo_bank.util.Cache;
import com.example.allo_bank.util.TypeEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static com.example.allo_bank.util.Constant.IDR;
import static com.example.allo_bank.util.Constant.SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class HistoricalGetStorageUsdServiceImplTest {

    @Autowired
    private HistoricalGetStorageUsdServiceImpl service;

    @MockitoBean
    private Cache cache;

    @Test
    void shouldReturnHistoricalIdrUsdResponseFromCache() {

        HistoricalIdrUsdDto mockDto = new HistoricalIdrUsdDto();
        mockDto.setBase(IDR);
        mockDto.setStartDate("2020-01-01");
        mockDto.setEndDate("2020-01-10");

        when(cache.getDataCache(TypeEnum.historical_idr_usd)).thenReturn(mockDto);

        ApiResponse<Object> result = service.fetchData();

        verify(cache).getDataCache(TypeEnum.historical_idr_usd);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(SUCCESS);
        assertThat(result.getResourceType()).isEqualTo("historical_idr_usd");

        assertThat(result.getData()).isInstanceOf(HistoricalIdrUsdResponse.class);
        HistoricalIdrUsdResponse responseData = (HistoricalIdrUsdResponse) result.getData();

        assertThat(responseData.getBase()).isEqualTo(IDR);
        assertThat(responseData.getStartDate()).isEqualTo("2020-01-01");
        assertThat(responseData.getEndDate()).isEqualTo("2020-01-10");
    }
}

