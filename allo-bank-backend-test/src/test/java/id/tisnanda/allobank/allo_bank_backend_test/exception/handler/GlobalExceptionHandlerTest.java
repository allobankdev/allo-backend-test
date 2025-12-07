package id.tisnanda.allobank.allo_bank_backend_test.exception.handler;

import id.tisnanda.allobank.allo_bank_backend_test.dto.ErrorResponse;
import id.tisnanda.allobank.allo_bank_backend_test.exception.AlloBankException;
import id.tisnanda.allobank.allo_bank_backend_test.exception.ErrorCodes;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ControllerAdvice
public class GlobalExceptionHandlerTest {
    private GlobalExceptionHandler handler;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/test");
    }

    @Test
    void testHandleAlloBankException() {
        AlloBankException ex = new AlloBankException("Data not found", ErrorCodes.RESOURCE_NOT_FOUND);

        ResponseEntity<ErrorResponse> responseEntity = handler.handleAlloBankException(ex, request);
        ErrorResponse response = responseEntity.getBody();

        Assertions.assertNotNull(response);
        Assertions.assertEquals("404", response.getCode());
        Assertions.assertEquals("Data not found", response.getMessage());
        Assertions.assertEquals("RESOURCE_NOT_FOUND", response.getError());
        Assertions.assertEquals("/api/test", response.getPath());
        Assertions.assertNotNull(response.getTimestamp());
    }

    @Test
    void testHandleUnexpectedException() {
        Exception ex = new RuntimeException("Internal server error");

        ResponseEntity<ErrorResponse> responseEntity = handler.handleUnexpectedException(ex, request);
        ErrorResponse response = responseEntity.getBody();

        Assertions.assertEquals("500", response.getCode());
        Assertions.assertEquals("Internal server error", response.getMessage());
        Assertions.assertEquals("RuntimeException", response.getError());
        Assertions.assertEquals("/api/test", response.getPath());
        Assertions.assertNotNull(response.getTimestamp());
    }
}
