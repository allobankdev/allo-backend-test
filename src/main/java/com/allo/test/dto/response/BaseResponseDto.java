package com.allo.test.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponseDto {

    private Integer code;
    private String message;
    private String description;
    private String errors;
    private String path;
    private Object data;

}
