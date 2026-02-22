package io.aditsukoco.allobank_test.services.finance;

import io.aditsukoco.allobank_test.models.dto.response.LatestDataResponseDTO;
import io.aditsukoco.allobank_test.models.enums.ResourceTypeEnum;
import io.aditsukoco.allobank_test.services.finance.strategy.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class FinanceServiceImplTests {

    @Mock
    private FinanceDataFetchStrategyFactory mockFinanceDataFetchStrategyFactory;

    @Mock
    private FetchLatestDataStrategy mockFetchLatestDataStrategy;

    @InjectMocks
    private FinanceServiceImpl financeService;

    @Test
    public void getFinanceDataTest_LatestData() {
        LatestDataResponseDTO expectedData = LatestDataResponseDTO.builder().build();

        when(mockFinanceDataFetchStrategyFactory.getStrategy(ResourceTypeEnum.LatestIDRRates))
                .thenReturn(mockFetchLatestDataStrategy);
        when(mockFetchLatestDataStrategy.fetchData()).thenReturn(expectedData);

        Object result = financeService.getFinanceData(ResourceTypeEnum.LatestIDRRates);

        assertEquals(expectedData, result);
    }

}
