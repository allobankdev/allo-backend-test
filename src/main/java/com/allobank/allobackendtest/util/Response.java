package com.allobank.allobackendtest.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Response<T> {
    private String message;
    private Object data;
    private int status;    
}
