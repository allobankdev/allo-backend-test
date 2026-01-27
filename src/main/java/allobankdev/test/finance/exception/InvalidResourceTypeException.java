package allobankdev.test.finance.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidResourceTypeException extends RuntimeException {
    public InvalidResourceTypeException(String type) {
        super("Invalid resource type: " + type);
    }
}
