package id.co.microservice.currency.currency_service.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CurrencyExceptionTest {

    @Test
    void testConstructorWithMessageAndStatus() {
        CurrencyException ex = new CurrencyException("Custom error", HttpStatus.NOT_FOUND);

        assertEquals("Custom error", ex.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }

    @Test
    void testConstructorWithMessage_DefaultsToBadRequest() {
        CurrencyException ex = new CurrencyException("Invalid input");

        assertEquals("Invalid input", ex.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

    @Test
    void testConstructorWithStatus_UsesReasonPhraseAsMessage() {
        CurrencyException ex = new CurrencyException(HttpStatus.INTERNAL_SERVER_ERROR);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ex.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatus());
    }
}
