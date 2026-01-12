package id.co.evan.project.aggregator.fault;

import id.co.evan.project.aggregator.util.ErrorCode;
import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException{
    private final ErrorCode errorCode;

    public ResourceNotFoundException(ErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }
}