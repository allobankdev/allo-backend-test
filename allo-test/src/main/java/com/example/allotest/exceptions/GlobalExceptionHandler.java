package com.example.allotest.exceptions;

import com.example.allotest.constants.ApiResponseConstants;
import com.example.allotest.dtos.BaseResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(CustomGlobalException.class)
    public ResponseEntity<BaseResponseDto<String>> handleException(CustomGlobalException ex) {
        logger.error(messageSource.getMessage("error.occured", new String[]{ex.getMessage()}, null));
        return ResponseEntity.internalServerError()
                .body(new BaseResponseDto<>(ApiResponseConstants.INTERNAL_SERVER_ERROR_STATUS_CODE,
                        ApiResponseConstants.INTERNAL_SERVER_ERROR_STATUS_MESSAGE, ex.getResourceType(), ex.getMessage()));
    }

    @ExceptionHandler(NoPathAvailableException.class)
    public ResponseEntity<BaseResponseDto<String>> handleNoPathAvailable(NoPathAvailableException ex) {
        logger.error(messageSource.getMessage("error.occured", new String[]{ex.getMessage()}, null));
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new BaseResponseDto<>(ApiResponseConstants.NOT_FOUND_STATUS_CODE,
                        ApiResponseConstants.NOT_FOUND_STATUS_MESSAGE, ex.getResourceType(), ex.getMessage()));
    }

}
