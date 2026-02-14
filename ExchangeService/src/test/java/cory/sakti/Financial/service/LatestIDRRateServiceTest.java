package cory.sakti.Financial.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LatestIDRRateServiceTest {

    private final String githubUser = "cory-work-tech";

    public BigDecimal calculateSpreadFactor(String username) {
        return BigDecimal.ZERO;
    }

    public BigDecimal calculateBuySpread(BigDecimal usdRate, BigDecimal factor) {
        return BigDecimal.ZERO;
    }


    @Test
    @DisplayName("Verify ASCII Spread Factor logic for specific username")
    void shouldCalculateCorrectlyForSpecificUser() {
        // Requirements:
        // 1. Sum ASCII of "cory-work-tech" = 1406
        // 2. 1406 % 1000 = 406
        // 3. 432 / 100000.0 = 0.00406
        BigDecimal expectedFactor = new BigDecimal("0.00406");

        // Act
        BigDecimal actualFactor = calculateSpreadFactor("cory-work-tech");

        // Assert Fails because method returns BigDecimal.ZERO)
        assertEquals(0, expectedFactor.compareTo(actualFactor),
                "Spread factor must match the ASCII sum requirement precisely");
    }


}
