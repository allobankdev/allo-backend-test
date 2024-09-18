package com.allobank.allobackendtest.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response<T> {
    private String status;
    private int statusCode;
    private String message;
    private T data;
}
