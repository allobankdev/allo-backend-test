package com.example.allobank.backend.test.takehometest.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseData<T> {
    private String messages;
    private T payload;
}
