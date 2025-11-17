package id.co.microservice.currency.currency_service.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyResponseDtoTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGettersAndSetters() {
        CurrencyResponseDto dto = new CurrencyResponseDto();
        dto.setCurrencies("USD, EUR");
        dto.setUsdBuySpreadIdr(15000.25);

        assertEquals("USD, EUR", dto.getCurrencies());
        assertEquals(15000.25, dto.getUsdBuySpreadIdr());
    }

    @Test
    void testEqualsAndHashCode() {
        CurrencyResponseDto dto1 = new CurrencyResponseDto();
        dto1.setCurrencies("USD");
        dto1.setUsdBuySpreadIdr(15000.25);

        CurrencyResponseDto dto2 = new CurrencyResponseDto();
        dto2.setCurrencies("USD");
        dto2.setUsdBuySpreadIdr(15000.25);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testJsonSerialization() throws Exception {
        CurrencyResponseDto dto = new CurrencyResponseDto();
        dto.setCurrencies("USD");
        dto.setUsdBuySpreadIdr(15000.25);

        String json = objectMapper.writeValueAsString(dto);

        assertTrue(json.contains("\"USD_BuySpread_IDR\":15000.25"));
        assertTrue(json.contains("\"currencies\":\"USD\""));
    }

    @Test
    void testJsonSerializationOmitsNulls() throws Exception {
        CurrencyResponseDto dto = new CurrencyResponseDto();
        dto.setCurrencies("USD");

        String json = objectMapper.writeValueAsString(dto);

        assertTrue(json.contains("\"currencies\":\"USD\""));
        assertFalse(json.contains("USD_BuySpread_IDR")); // should be omitted
    }
}
