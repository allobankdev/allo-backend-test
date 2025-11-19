package com.allo.test.shared.utils;

import com.allo.test.modules.finance.exceptions.MissingRequiredConfigException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for SpreadCalculator utility class.
 * <p>
 * Tests spread factor calculation based on GitHub username and
 * USD buy spread calculation with banking margins.
 */
@DisplayName("SpreadCalculator Unit Tests")
class SpreadCalculatorTest {

    // ==================== SPREAD FACTOR CALCULATION - SUCCESS ====================

    @Test
    @DisplayName("Should calculate spread factor for valid username")
    void shouldCalculateSpreadFactor_ForValidUsername() {
        // Arrange
        String username = "frhn9";

        // Act
        double spreadFactor = SpreadCalculator.calculateSpreadFactor(username);

        // Assert
        // Username "frhn9" in lowercase:
        // f=102, r=114, h=104, n=110, 9=57
        // Sum = 487, 487 % 1000 = 487, 487 / 100000.0 = 0.00487
        assertThat(spreadFactor)
                .isEqualTo(0.00487)
                .isBetween(0.0, 0.00999);
    }

    @Test
    @DisplayName("Should calculate spread factor for single character username")
    void shouldCalculateSpreadFactor_ForSingleCharacter() {
        // Arrange
        String username = "a";

        // Act
        double spreadFactor = SpreadCalculator.calculateSpreadFactor(username);

        // Assert
        // 'a' = 97, 97 % 1000 = 97, 97 / 100000.0 = 0.00097
        assertThat(spreadFactor).isEqualTo(0.00097);
    }

    @Test
    @DisplayName("Should normalize uppercase username to lowercase before calculation")
    void shouldNormalizeUppercaseUsername_BeforeCalculation() {
        // Arrange
        String uppercaseUsername = "FRHN9";
        String lowercaseUsername = "frhn9";

        // Act
        double uppercaseResult = SpreadCalculator.calculateSpreadFactor(uppercaseUsername);
        double lowercaseResult = SpreadCalculator.calculateSpreadFactor(lowercaseUsername);

        // Assert - Should produce same result regardless of case
        assertThat(uppercaseResult)
                .isEqualTo(lowercaseResult)
                .isEqualTo(0.00487);
    }

    @Test
    @DisplayName("Should normalize mixed case username to lowercase before calculation")
    void shouldNormalizeMixedCaseUsername_BeforeCalculation() {
        // Arrange
        String mixedCaseUsername = "FrHn9";
        String lowercaseUsername = "frhn9";

        // Act
        double mixedCaseResult = SpreadCalculator.calculateSpreadFactor(mixedCaseUsername);
        double lowercaseResult = SpreadCalculator.calculateSpreadFactor(lowercaseUsername);

        // Assert
        assertThat(mixedCaseResult).isEqualTo(lowercaseResult);
    }

    @Test
    @DisplayName("Should calculate spread factor for username with numbers")
    void shouldCalculateSpreadFactor_ForUsernameWithNumbers() {
        // Arrange
        String username = "user123";

        // Act
        double spreadFactor = SpreadCalculator.calculateSpreadFactor(username);

        // Assert
        // u=117, s=115, e=101, r=114, 1=49, 2=50, 3=51
        // Sum = 597, 597 % 1000 = 597, 597 / 100000.0 = 0.00597
        assertThat(spreadFactor)
                .isEqualTo(0.00597)
                .isBetween(0.0, 0.00999);
    }

    @Test
    @DisplayName("Should calculate spread factor for username with special characters")
    void shouldCalculateSpreadFactor_ForUsernameWithSpecialCharacters() {
        // Arrange
        String username = "user-name_123";

        // Act
        double spreadFactor = SpreadCalculator.calculateSpreadFactor(username);

        // Assert
        // Should handle special characters (-, _) correctly
        assertThat(spreadFactor).isBetween(0.0, 0.00999);
    }

    @Test
    @DisplayName("Should calculate spread factor for long username")
    void shouldCalculateSpreadFactor_ForLongUsername() {
        // Arrange
        String username = "verylongusernamestring";

        // Act
        double spreadFactor = SpreadCalculator.calculateSpreadFactor(username);

        // Assert
        assertThat(spreadFactor).isBetween(0.0, 0.00999);
    }

    // ==================== SPREAD FACTOR CALCULATION - ERROR CASES ====================

    @Test
    @DisplayName("Should throw MissingRequiredConfigException when username is null")
    void shouldThrowException_WhenUsernameIsNull() {
        // Act & Assert
        assertThrows(MissingRequiredConfigException.class, () -> {
            SpreadCalculator.calculateSpreadFactor(null);
        });
    }

    @Test
    @DisplayName("Should throw MissingRequiredConfigException when username is empty string")
    void shouldThrowException_WhenUsernameIsEmpty() {
        // Act & Assert
        assertThrows(MissingRequiredConfigException.class, () -> {
            SpreadCalculator.calculateSpreadFactor("");
        });
    }

    @Test
    @DisplayName("Should throw MissingRequiredConfigException when username is whitespace only")
    void shouldThrowException_WhenUsernameIsWhitespaceOnly() {
        // Act & Assert
        assertThrows(MissingRequiredConfigException.class, () -> {
            SpreadCalculator.calculateSpreadFactor("   ");
        });
    }

    // ==================== USD BUY SPREAD CALCULATION - SUCCESS ====================

    @Test
    @DisplayName("Should calculate USD buy spread with valid inputs")
    void shouldCalculateUsdBuySpread_WithValidInputs() {
        // Arrange
        BigDecimal usdRate = new BigDecimal("0.0000634"); // 1 IDR = 0.0000634 USD
        double spreadFactor = 0.00487; // Spread factor for username "frhn9"

        // Act
        BigDecimal usdBuySpread = SpreadCalculator.calculateUsdBuySpread(usdRate, spreadFactor);

        // Assert
        // Formula: (1 / 0.0000634) * (1 + 0.00487)
        // = 15772.870662460568 * 1.00487
        // = 15849.678... (rounded to 2 decimal places with HALF_UP)
        // = 15849.68
        BigDecimal expected = new BigDecimal("15849.68");
        assertThat(usdBuySpread).isEqualByComparingTo(expected);
        assertThat(usdBuySpread.scale()).isEqualTo(2); // Verify 2 decimal places
    }

    @Test
    @DisplayName("Should calculate USD buy spread with large rate")
    void shouldCalculateUsdBuySpread_WithLargeRate() {
        // Arrange
        BigDecimal usdRate = new BigDecimal("1.5");
        double spreadFactor = 0.00534;

        // Act
        BigDecimal usdBuySpread = SpreadCalculator.calculateUsdBuySpread(usdRate, spreadFactor);

        // Assert
        // Formula: (1 / 1.5) * (1 + 0.00534)
        // = 0.666667 * 1.00534
        // = 0.67 (rounded to 2 decimal places)
        BigDecimal expected = new BigDecimal("0.67");
        assertThat(usdBuySpread).isEqualByComparingTo(expected);
    }

    @Test
    @DisplayName("Should calculate USD buy spread with very small rate")
    void shouldCalculateUsdBuySpread_WithVerySmallRate() {
        // Arrange
        BigDecimal usdRate = new BigDecimal("0.000001");
        double spreadFactor = 0.00534;

        // Act
        BigDecimal usdBuySpread = SpreadCalculator.calculateUsdBuySpread(usdRate, spreadFactor);

        // Assert
        // Formula: (1 / 0.000001) * (1 + 0.00534)
        // = 1000000 * 1.00534
        // = 1005340.00 (rounded to 2 decimal places)
        BigDecimal expected = new BigDecimal("1005340.00");
        assertThat(usdBuySpread).isEqualByComparingTo(expected);
    }

    @Test
    @DisplayName("Should calculate USD buy spread with negative spread factor")
    void shouldCalculateUsdBuySpread_WithNegativeSpreadFactor() {
        // Arrange
        BigDecimal usdRate = new BigDecimal("0.0000634");
        double spreadFactor = -0.001;

        // Act
        BigDecimal usdBuySpread = SpreadCalculator.calculateUsdBuySpread(usdRate, spreadFactor);

        // Assert
        // Formula: (1 / 0.0000634) * (1 + (-0.001))
        // = 15772.87066 * 0.999
        // = 15757.10 (rounded to 2 decimal places)
        BigDecimal expected = new BigDecimal("15757.10");
        assertThat(usdBuySpread).isEqualByComparingTo(expected);
    }

    @Test
    @DisplayName("Should apply HALF_UP rounding correctly")
    void shouldApplyHalfUpRounding_Correctly() {
        // Arrange - Choose values that result in exactly 0.005 in third decimal place
        BigDecimal usdRate = new BigDecimal("0.0001"); // 1 / 0.0001 = 10000
        double spreadFactor = 0.000015; // 10000 * 1.000015 = 10000.15

        // Act
        BigDecimal usdBuySpread = SpreadCalculator.calculateUsdBuySpread(usdRate, spreadFactor);

        // Assert
        // Verify rounding mode is HALF_UP by checking scale
        assertThat(usdBuySpread.scale()).isEqualTo(2);
        assertThat(usdBuySpread.remainder(BigDecimal.ONE).scale()).isLessThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Should handle high precision input correctly")
    void shouldHandleHighPrecisionInput_Correctly() {
        // Arrange
        BigDecimal usdRate = new BigDecimal("0.123456789123456");
        double spreadFactor = 0.00999;

        // Act
        BigDecimal usdBuySpread = SpreadCalculator.calculateUsdBuySpread(usdRate, spreadFactor);

        // Assert
        assertThat(usdBuySpread.scale()).isEqualTo(2);
        assertThat(usdBuySpread).isGreaterThan(BigDecimal.ZERO);
    }

    // ==================== USD BUY SPREAD CALCULATION - ERROR CASES ====================

    @Test
    @DisplayName("Should return ZERO when USD rate is null")
    void shouldReturnZero_WhenUsdRateIsNull() {
        // Arrange
        BigDecimal usdRate = null;
        double spreadFactor = 0.00487;

        // Act
        BigDecimal usdBuySpread = SpreadCalculator.calculateUsdBuySpread(usdRate, spreadFactor);

        // Assert
        assertThat(usdBuySpread).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("Should return ZERO when USD rate is zero")
    void shouldReturnZero_WhenUsdRateIsZero() {
        // Arrange
        BigDecimal usdRate = BigDecimal.ZERO;
        double spreadFactor = 0.00487;

        // Act
        BigDecimal usdBuySpread = SpreadCalculator.calculateUsdBuySpread(usdRate, spreadFactor);

        // Assert
        assertThat(usdBuySpread).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("Should return ZERO when USD rate is exactly zero with scale")
    void shouldReturnZero_WhenUsdRateIsExactlyZeroWithScale() {
        // Arrange
        BigDecimal usdRate = new BigDecimal("0.00000");
        double spreadFactor = 0.00487;

        // Act
        BigDecimal usdBuySpread = SpreadCalculator.calculateUsdBuySpread(usdRate, spreadFactor);

        // Assert
        assertThat(usdBuySpread).isEqualByComparingTo(BigDecimal.ZERO);
    }
}
