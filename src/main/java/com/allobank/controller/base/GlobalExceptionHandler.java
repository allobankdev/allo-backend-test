package com.allobank.controller.base;

import com.allobank.dto.base.BaseResponse;
import com.allobank.exceptions.BusinessException;
import com.allobank.exceptions.ExternalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.allobank.enums.Commons.STATUS_FAILED;
import static com.allobank.enums.Commons.STATUS_SUCCESS;
import static com.allobank.enums.RESPONSE.GENERAL_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends BaseController {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Void>> handleInternal(Exception ex) {
        log.error("Exception: ", ex);
        return createUnknownErrorResponse(BaseResponse.<Void>builder()
                .status(STATUS_FAILED.getValue())
                .code(GENERAL_ERROR.getCode())
                .message(GENERAL_ERROR.getMessage())
                .data(null)
                .build());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<BaseResponse<Void>> handleBusiness(BusinessException ex) {
        log.error("BusinessException: ", ex);
        return createBadRequestResponse(BaseResponse.<Void>builder()
                .status(STATUS_SUCCESS.getValue())
                .code(ex.getCode())
                .message(ex.getMessage())
                .data(null)
                .build());
    }

    @ExceptionHandler(ExternalException.class)
    public ResponseEntity<BaseResponse<Void>> handleExternal(ExternalException ex) {
        log.error("ExternalException: ", ex);
        return createUnknownErrorResponse(BaseResponse.<Void>builder()
                .status(STATUS_SUCCESS.getValue())
                .code(ex.getCode())
                .message(ex.getMessage())
                .data(null)
                .build());
    }

}
