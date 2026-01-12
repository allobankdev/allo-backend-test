package id.co.evan.project.aggregator.controller;

import id.co.evan.project.aggregator.config.ApiKeyFilter;
import id.co.evan.project.aggregator.model.response.UnifiedFinanceResponse;
import id.co.evan.project.aggregator.service.DataStoreService;
import id.co.evan.project.aggregator.util.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@WebFluxTest(controllers = FinanceController.class)
class FinanceControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @MockitoBean
    DataStoreService dataStoreService;

    @MockitoBean
    ApiKeyFilter apiKeyFilter;

    @BeforeEach
    void setUp() {
        Mockito.when(apiKeyFilter.filter(any(), any()))
            .thenAnswer(invocation -> {
                ServerWebExchange exchange = invocation.getArgument(0);
                WebFilterChain chain = invocation.getArgument(1);
                return chain.filter(exchange);
            });
    }

    @Test
    void getFinanceData_success_200() {
        String resourceType = Constants.LATEST_IDR_RATES;

        var response = new UnifiedFinanceResponse(
            resourceType,
            ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSXXX")),
            List.of(Map.of(
                "currency", "USD",
                "rate", "rate",
                "USD_BuySpread_IDR", BigDecimal.valueOf(15625.0)
            ))
        );

        Mockito.when(dataStoreService.isReady()).thenReturn(true);
        Mockito.when(dataStoreService.getData(anyString())).thenReturn(Mono.just(response));

        webTestClient.get()
            .uri("/api/finance/data/{resourceType}", resourceType)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.resourceType").isEqualTo(resourceType)
            .jsonPath("$.data[0].currency").isEqualTo("USD");
    }
}
