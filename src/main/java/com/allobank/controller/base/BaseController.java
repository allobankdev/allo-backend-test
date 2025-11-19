package com.allobank.controller.base;

import com.allobank.dto.base.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.allobank.enums.Commons.STATUS_SUCCESS;
import static com.allobank.enums.RESPONSE.SUCCESS;

public abstract class BaseController {

    protected <RES> ResponseEntity<BaseResponse<RES>> createSuccessResponse(RES response) {
        return ResponseEntity.ok().body(BaseResponse.<RES>builder()
                .status(STATUS_SUCCESS.getValue())
                .code(SUCCESS.getCode())
                .message(SUCCESS.getMessage())
                .data(response)
                .build());
    }

    protected <RES> ResponseEntity<BaseResponse<RES>> createUnknownErrorResponse(BaseResponse<RES> response) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    protected <RES> ResponseEntity<BaseResponse<RES>> createBadRequestResponse(BaseResponse<RES> response) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


}
