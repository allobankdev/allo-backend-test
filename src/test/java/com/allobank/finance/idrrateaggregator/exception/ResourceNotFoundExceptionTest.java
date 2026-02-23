package com.allobank.finance.idrrateaggregator.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceNotFoundExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        String message = "Resource type not found";

        ResourceNotFoundException exception =
                new ResourceNotFoundException(message);

        assertThat(exception)
                .isInstanceOf(RuntimeException.class);
        assertThat(exception.getMessage())
                .isEqualTo(message);
    }

    @Test
    void shouldBeAnnotatedWithNotFoundResponseStatus() {
        ResponseStatus responseStatus =
                ResourceNotFoundException.class
                        .getAnnotation(ResponseStatus.class);

        assertThat(responseStatus).isNotNull();
        assertThat(responseStatus.value())
                .isEqualTo(HttpStatus.NOT_FOUND);
    }
}
