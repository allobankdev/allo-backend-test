package com.allobank.allobackendtest.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseData<T> {
    private String message;
    private Integer code;
    private T data;
}
