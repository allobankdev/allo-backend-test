package com.allo_backend_test.finance;

import com.allo_backend_test.finance.Utils.Const;
import com.allo_backend_test.finance.adapter.LatestIdrRatesFetcher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LatestIdrRatesFetcherTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private LatestIdrRatesFetcher fetcher;

    @Test
    void shouldCalculateSpread() {
        ReflectionTestUtils.setField(fetcher, "githubUsername", "rhayatod");

        Map<String, Object> mockResponse = Map.of(
                "rates", Map.of(Const.USD, 0.000065)
        );

        when(restTemplate.getForObject(anyString(), eq(Map.class)))
                .thenReturn(mockResponse);

        Object result = fetcher.fetchAndTransform();

        assertNotNull(result);
    }
}
