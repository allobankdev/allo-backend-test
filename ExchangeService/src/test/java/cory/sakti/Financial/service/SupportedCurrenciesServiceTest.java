package cory.sakti.Financial.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class SupportedCurrenciesServiceTest {

    private SupportedCurrenciesService service;

    @BeforeEach
    void setUp() {
        service = new SupportedCurrenciesService();
    }

    @Test
    @DisplayName("Service must return a sealed/immutable Map")
    void shouldFailWhenMapIsMutable() throws Exception {
        String json = "{\"USD\":\"United States Dollar\"}";
        JsonNode node = new ObjectMapper().readTree(json);

        Map<String, String> result = (Map<String, String>) service.transform(node);

        // This will FAIL because the skeleton returns a standard HashMap
        assertThrows(UnsupportedOperationException.class, () ->
                        result.put("IDR", "Indonesian Rupiah"),
                "Constraint C: Service must return an immutable collection"
        );
    }
}
