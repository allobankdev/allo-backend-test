package test.allo.backend.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ObjectMapper mapper;

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<JsonNode> handleExternalApiException(IllegalStateException e) {
        ObjectNode response = mapper.createObjectNode();
        response.put("status", 403);
        response.put("error", "Operation Not Allowed");
        response.put("message", e.getMessage());

        return ResponseEntity.status(409).body(response);
    }

}
