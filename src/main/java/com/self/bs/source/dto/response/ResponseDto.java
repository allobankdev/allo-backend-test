package com.self.bs.source.dto.response;

import java.time.LocalDateTime;

public class ResponseDto<T> {
    private LocalDateTime timestamp;
    private T data;
    private String errorMessage;

    public ResponseDto(T data){
        this.timestamp = LocalDateTime.now();
        this.data = data;
    }

    public ResponseDto(String errorMessage){
        this.timestamp = LocalDateTime.now();
        this.errorMessage = errorMessage;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "ResponseDto [timestamp=" + timestamp + ", data=" + data + ", errorMessage=" + errorMessage + "]";
    }
}
