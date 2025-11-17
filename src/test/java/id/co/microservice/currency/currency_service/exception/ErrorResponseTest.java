package id.co.microservice.currency.currency_service.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Test
    void testConstructorSetsFields() {
        ErrorResponse errorResponse = new ErrorResponse(404, "Not Found", "Resource missing");

        assertEquals(404, errorResponse.getStatus());
        assertEquals("Not Found", errorResponse.getError());
        assertEquals("Resource missing", errorResponse.getMessage());
        assertNotNull(errorResponse.getTimestamp());
    }

    @Test
    void testTimestampIsRecent() {
        ErrorResponse errorResponse = new ErrorResponse(400, "Bad Request", "Invalid input");
        LocalDateTime now = LocalDateTime.now();

        assertTrue(errorResponse.getTimestamp().isBefore(now.plusSeconds(2)));
        assertTrue(errorResponse.getTimestamp().isAfter(now.minusSeconds(2)));
    }

    @Test
    void testJsonSerialization() throws Exception {
        ErrorResponse errorResponse = new ErrorResponse(500, "Internal Server Error", "Unexpected failure");

        String json = objectMapper.writeValueAsString(errorResponse);

        assertTrue(json.contains("\"status\":500"));
        assertTrue(json.contains("\"error\":\"Internal Server Error\""));
        assertTrue(json.contains("\"message\":\"Unexpected failure\""));
        assertTrue(json.contains("timestamp")); // timestamp should be present
    }
}
