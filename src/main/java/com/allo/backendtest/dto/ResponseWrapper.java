package com.allo.backendtest.dto;

import com.allo.backendtest.constant.ResponseStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ResponseWrapper {

    private ResponseStatus status = ResponseStatus.SUCCESS;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    private Object data;
}
