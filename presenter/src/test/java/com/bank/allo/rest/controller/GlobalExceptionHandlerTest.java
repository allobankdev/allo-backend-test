package com.bank.allo.rest.controller;

import com.bank.allo.exception.BadRequestException;
import com.bank.allo.exception.NotFoundException;
import com.bank.allo.rest.entity.ApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleBadRequest_returns400() {
        BadRequestException ex = new BadRequestException("bad input");
        ResponseEntity<ApiResponse<Void>> resp = handler.handleBadRequest(ex);

        assertEquals(400, resp.getStatusCode().value());
        assertEquals("bad input", resp.getBody().getMessage());
        assertEquals(ApiResponse.Source.APPLICATION, resp.getBody().getSource());
    }

    @Test
    void handleNotFound_returns404() {
        NotFoundException ex = new NotFoundException("not found");
        ResponseEntity<ApiResponse<Void>> resp = handler.handleNotFound(ex);

        assertEquals(404, resp.getStatusCode().value());
        assertEquals("not found", resp.getBody().getMessage());
    }

    @Test
    void handleTypeMismatch_returns400() throws NoSuchMethodException {
        // Build dummy MethodArgumentTypeMismatchException
        MethodArgumentTypeMismatchException ex = new MethodArgumentTypeMismatchException(
                "abc",
                Integer.class,
                "age",
                null,
                new IllegalArgumentException("not a number"));

        ResponseEntity<ApiResponse<Void>> resp = handler.handleTypeMismatch(ex);

        assertEquals(400, resp.getStatusCode().value());
        assertTrue(resp.getBody().getMessage().contains("Invalid value for 'age'"));
    }

    @Test
    void handleMissingServletRequestParameter_returns400() {
        MissingServletRequestParameterException ex = new MissingServletRequestParameterException("id", "String");

        ResponseEntity<ApiResponse<Void>> resp = handler.handleMissingParam(ex);

        assertEquals(400, resp.getStatusCode().value());
        assertEquals("Missing required parameter: id", resp.getBody().getMessage());
    }

    @Test
    void handleMissingPathVariable_returns400() throws NoSuchMethodException {

        Method method = this.getClass().getDeclaredMethod("dummyMethod", String.class);
        MethodParameter param = new MethodParameter(method, 0);

        MissingPathVariableException ex = new MissingPathVariableException("userId", param);

        ResponseEntity<ApiResponse<Void>> resp = handler.handleMissingPath(ex);

        assertEquals(400, resp.getStatusCode().value());
        assertTrue(resp.getBody().getMessage().contains("Missing path variable: userId"));
    }

    private void dummyMethod(String userId) {}

    @Test
    void handleUnexpected_returns500() {
        Exception ex = new Exception("boom");
        ResponseEntity<ApiResponse<Void>> resp = handler.handleUnexpected(ex);

        assertEquals(500, resp.getStatusCode().value());
        assertEquals("Unexpected error occurred", resp.getBody().getMessage());
        assertEquals(ApiResponse.Source.UNKNOWN, resp.getBody().getSource());
    }
}
