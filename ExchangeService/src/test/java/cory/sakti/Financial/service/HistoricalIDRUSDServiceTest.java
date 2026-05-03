package cory.sakti.Financial.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class HistoricalIDRUSDServiceTest {

    private HistoricalIDRUSDService service;

    @BeforeEach
    void setUp() {
        service = new HistoricalIDRUSDService();
    }

    @Test
    @DisplayName("Historical data must be deeply immutable (Outer and Inner maps)")
    void shouldFailIfNestedMapIsMutable() throws Exception {
        //dummy json
        String json = """
            {
                "rates": {
                    "2024-01-01": { "USD": 0.000064 }
                }
            }
            """;
        JsonNode node = new ObjectMapper().readTree(json);

        // This might throw a ClassCastException if the skeleton returns the wrong thing
        Map<String, Map<String, BigDecimal>> result =
                (Map<String, Map<String, BigDecimal>>) service.transform(node);

        // Assert
        assertNotNull(result);


        // It should throw UnsupportedOperationException if the inner map is immutable.
        assertThrows(UnsupportedOperationException.class, () -> {
            result.get("2024-01-01").put("USD", new BigDecimal("1.0"));
        }, "Constraint C Failure: The inner rate map for a specific date is still mutable!");
    }
}
