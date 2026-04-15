package id.co.allobank.exchangerate.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import id.co.allobank.exchangerate.common.Constant;
import id.co.allobank.exchangerate.dto.BaseResponseDTO;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Handle CustomException
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<BaseResponseDTO<Object>> handleCustomException(CustomException ex) {

        log.error("CustomException occurred: {}", ex.getMessage(), ex);

        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(BaseResponseDTO.error(ex.getResponseCode(), ex.getMessage()));
    }

    // Handle generic exception (fallback)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponseDTO<Object>> handleException(Exception ex) {

        log.error("Unhandled Exception: {}", ex.getMessage(), ex);

        return ResponseEntity
                .internalServerError()
                .body(BaseResponseDTO.error(
                        Constant.GENERAL_ERROR,
                        "Internal Server Error"
                ));
    }
}