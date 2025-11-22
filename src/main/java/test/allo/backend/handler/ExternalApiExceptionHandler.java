package test.allo.backend.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import test.allo.backend.exception.ExternalApiException;

@RestControllerAdvice
@RequiredArgsConstructor
public class ExternalApiExceptionHandler {

    private final ObjectMapper mapper;

    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<JsonNode> handleExternalApiException(ExternalApiException e) {
        ObjectNode response = mapper.createObjectNode();
        response.put("status", e.getStatus());
        response.put("error", e.getError());
        response.put("message", e.getMessage());

        return ResponseEntity.status(e.getStatus()).body(response);
    }
}
