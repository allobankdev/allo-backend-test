package id.co.microservice.currency.currency_service.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FrankfurterResponseDtoTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGettersAndSetters() {
        FrankfurterResponseDto dto = new FrankfurterResponseDto();
        dto.setAmount(100.0);
        dto.setBase("IDR");
        dto.setDate("2024-01-05");
        dto.setRates("{USD:0.000065}");
        dto.setStartDate("2024-01-01");
        dto.setEndDate("2024-01-05");

        assertEquals(100.0, dto.getAmount());
        assertEquals("IDR", dto.getBase());
        assertEquals("2024-01-05", dto.getDate());
        assertEquals("{USD:0.000065}", dto.getRates());
        assertEquals("2024-01-01", dto.getStartDate());
        assertEquals("2024-01-05", dto.getEndDate());
    }

    @Test
    void testEqualsAndHashCode() {
        FrankfurterResponseDto dto1 = new FrankfurterResponseDto();
        dto1.setBase("IDR");
        dto1.setDate("2024-01-05");

        FrankfurterResponseDto dto2 = new FrankfurterResponseDto();
        dto2.setBase("IDR");
        dto2.setDate("2024-01-05");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToStringContainsFields() {
        FrankfurterResponseDto dto = new FrankfurterResponseDto();
        dto.setBase("USD");
        dto.setAmount(50.0);

        String toString = dto.toString();
        assertTrue(toString.contains("USD"));
        assertTrue(toString.contains("50.0"));
    }

    @Test
    void testJsonSerializationOmitsNulls() throws Exception {
        FrankfurterResponseDto dto = new FrankfurterResponseDto();
        dto.setBase("USD");
        dto.setAmount(50.0);

        String json = objectMapper.writeValueAsString(dto);

        assertTrue(json.contains("\"base\":\"USD\""));
        assertTrue(json.contains("\"amount\":50.0"));
        assertFalse(json.contains("date"));
        assertFalse(json.contains("rates"));
        assertFalse(json.contains("startDate"));
        assertFalse(json.contains("endDate"));
    }

    @Test
    void testJsonDeserialization() throws Exception {
        String json = "{\"amount\":100.0,\"base\":\"EUR\",\"date\":\"2024-01-05\"}";

        FrankfurterResponseDto dto = objectMapper.readValue(json, FrankfurterResponseDto.class);

        assertEquals(100.0, dto.getAmount());
        assertEquals("EUR", dto.getBase());
        assertEquals("2024-01-05", dto.getDate());
    }
}
