package org.allobanktest.common.exception;

public class InvalidResourceTypeException extends RuntimeException {
    public InvalidResourceTypeException(String resourceType) {
        super("Unknown resourceType: " + resourceType);
    }
}
