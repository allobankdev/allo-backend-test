package com.musfiul.idrrateaggregator.dto.api;

import lombok.*;
import org.springframework.http.HttpStatus;

@Setter
@Getter
@ToString
@AllArgsConstructor
@Builder
public class APIResponseDTO {
    private Boolean success;
    private String errMessage;
    private Object data;
    private HttpStatus httpStatus;
}
