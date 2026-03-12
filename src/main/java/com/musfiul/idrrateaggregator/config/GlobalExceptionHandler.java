package com.musfiul.idrrateaggregator.config;

import com.musfiul.idrrateaggregator.dto.api.APIResponseDTO;
import com.musfiul.idrrateaggregator.exception.BadRequestException;
import com.musfiul.idrrateaggregator.exception.ExternalApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class GlobalExceptionHandler {
    @ExceptionHandler(ExternalApiException.class)
    public final ResponseEntity<APIResponseDTO> error(ExternalApiException ex) {
        log.error(ex.getMessage(), ex);
        APIResponseDTO apiResponseDTO = new APIResponseDTO(false, ex.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(apiResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<APIResponseDTO> badRequest(BadRequestException ex) {
        log.error(ex.getMessage(), ex);
        APIResponseDTO apiResponseDTO = new APIResponseDTO(false, ex.getMessage(), null, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(apiResponseDTO, HttpStatus.BAD_REQUEST);
    }
}
