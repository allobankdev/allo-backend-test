package id.tisnanda.allobank.allo_bank_backend_test.exception.handler;

import id.tisnanda.allobank.allo_bank_backend_test.constant.Constant;
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
        when(request.getRequestURI()).thenReturn(Constant.TEST_API_URI);
    }

    @Test
    void testHandleAlloBankException() {
        AlloBankException ex = new AlloBankException(Constant.DATA_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND);

        ResponseEntity<ErrorResponse> responseEntity = handler.handleAlloBankException(ex, request);
        ErrorResponse response = responseEntity.getBody();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(Constant.RESOURCE_NOT_FOUND_CODE, response.getCode());
        Assertions.assertEquals(Constant.DATA_NOT_FOUND, response.getMessage());
        Assertions.assertEquals(Constant.RESOURCE_NOT_FOUND, response.getError());
        Assertions.assertEquals(Constant.TEST_API_URI, response.getPath());
        Assertions.assertNotNull(response.getTimestamp());
    }

    @Test
    void testHandleUnexpectedException() {
        Exception ex = new RuntimeException(Constant.INTERNAL_SERVER_ERROR);

        ResponseEntity<ErrorResponse> responseEntity = handler.handleUnexpectedException(ex, request);
        ErrorResponse response = responseEntity.getBody();

        Assertions.assertEquals(Constant.INTERNAL_SERVER_ERROR_CODE, response.getCode());
        Assertions.assertEquals(Constant.INTERNAL_SERVER_ERROR, response.getMessage());
        Assertions.assertEquals(Constant.RUNTIME_EXCEPTION, response.getError());
        Assertions.assertEquals(Constant.TEST_API_URI, response.getPath());
        Assertions.assertNotNull(response.getTimestamp());
    }
}
