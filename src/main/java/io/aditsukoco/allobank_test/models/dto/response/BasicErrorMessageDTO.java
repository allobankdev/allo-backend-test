package io.aditsukoco.allobank_test.models.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BasicErrorMessageDTO {
    @JsonProperty("message")
    private String message;
}
