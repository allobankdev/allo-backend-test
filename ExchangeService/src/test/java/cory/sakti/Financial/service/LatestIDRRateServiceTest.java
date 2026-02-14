package cory.sakti.Financial.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import cory.sakti.Financial.dto.IDRRateData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LatestIDRRateServiceTest {

    private LatestIDRRateService strategy;
    private final String githubUser = "cory-work-tech";

    @BeforeEach
    void setUp() {
        strategy = new LatestIDRRateService(githubUser);
    }

    @Test
    @DisplayName("Verify ASCII Spread Factor logic for specific username")
    void shouldCalculateCorrectlyForSpecificUser() {
        // Requirements:
        // 1. Sum ASCII of "cory-work-tech" = 1406
        // 2. 1406 % 1000 = 406
        // 3. 406 / 100000.0 = 0.00406
        BigDecimal expectedFactor = new BigDecimal("0.00406");

        // Act
        BigDecimal actualFactor = strategy.calculateSpreadFactor("cory-work-tech");

        // Assert Fails because method returns BigDecimal.ZERO)
        assertEquals(0, expectedFactor.compareTo(actualFactor),
                "Spread factor must match the ASCII sum requirement precisely");
    }

    @Test
    @DisplayName("Calculate Buy Spread using (1/rate) * (1+factor)")
    void shouldCalculateBuySpread() {
        //assuming rate, $1 = IDR 15625, 1/15625=0.000064
        BigDecimal rate = new BigDecimal("0.000064");
        BigDecimal factor = new BigDecimal("0.00406");

        // (1 / 0.000064) * 1.00406 = 15688.4375
        BigDecimal expected = new BigDecimal("15688.4375");
        BigDecimal actual = strategy.calculateBuySpread(rate, factor);

        // This will fail because skeleton returns ZERO
        assertEquals(0, expected.compareTo(actual), "Financial math incorrect");
    }


    @Test
    @DisplayName("Should calculate precise spread and return immutable record")
    void latestStrategy_ShouldCalculateCorrectly() throws Exception {
        // Arrange
        String json = "{\"base\":\"IDR\",\"date\":\"2026-02-14\",\"rates\":{\"USD\":0.000064}}";
        JsonNode node = new ObjectMapper().readTree(json);

        // Act
        IDRRateData result = (IDRRateData) strategy.transform(node);

        // (1 / 0.000064) * 1.00406 = 15688.4375
        BigDecimal expectedBuySpread = new BigDecimal("15688.4375");

        // Assert
        assertAll("Strategy Logic and Immutability",
                () -> assertEquals(0, expectedBuySpread.compareTo(result.usdBuySpreadIdr()), "Math mismatch"),
                () -> assertEquals(0, new BigDecimal("0.00406").compareTo(result.spreadFactorUsed())),
                () ->assertThrows(UnsupportedOperationException.class, () ->
                        result.rates().put("EUR", BigDecimal.ONE), "Data must be immutable")
        );
    }

}
