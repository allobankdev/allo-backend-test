package com.allobank.allobackendtest.responses;

import lombok.Data;

@Data
public class ResponseMsg<T> {

    private String rc;
    private String rm;
    private T data;

    @Override
    public String toString() {
        return "ResponseMsg{" +
                "rc='" + rc + '\'' +
                ", rm='" + rm + '\'' +
                ", data=" + data +
                '}';
    }

}
