package io.aditsukoco.allobank_test.exceptions;

import io.aditsukoco.allobank_test.models.dto.response.BasicErrorMessageDTO;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
public abstract class BaseRestException extends RuntimeException {

    protected int httpCode;
    protected String message;

    public ResponseEntity<BasicErrorMessageDTO> toResponseEntity() {
        BasicErrorMessageDTO messageDTO = new BasicErrorMessageDTO();
        messageDTO.setMessage(this.message);
        return ResponseEntity.status(httpCode).body(messageDTO);
    }
}
