package com.amri.apiintegration.util;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"status", "message", "data", "metadata"})
public class IResultDTO<T> {
    private int status;
    private String message;
    private T data;
    private Metadata metadata;


    public IResultDTO(int status, String message, T data){
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
