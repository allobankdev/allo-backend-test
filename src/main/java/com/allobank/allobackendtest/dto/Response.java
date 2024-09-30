package com.allobank.allobackendtest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    private String rc;
    private String message;
    private Object errTech;
    private String source;
    private Object data;
}
