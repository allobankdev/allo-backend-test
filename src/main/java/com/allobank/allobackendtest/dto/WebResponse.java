package com.allobank.allobackendtest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record WebResponse<T>(
        T data,
        PagingResponse paging,
        String error
){
}
