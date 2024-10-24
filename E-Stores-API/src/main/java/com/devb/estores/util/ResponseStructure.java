package com.devb.estores.util;

import lombok.Builder;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Builder
public class ResponseStructure<T> {
    private int status;
    private String message;
    private T data;

    public ResponseStructure<T> setStatus(int status) {
       this.status = status;
       return this;
    }

    public ResponseStructure<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public ResponseStructure<T> setData(T data) {
        this.data = data;
        return this;
    }

    public static <T> ResponseStructure<T> create(int status, String message, T data) {
        return ResponseStructure.<T>builder()
                .status(status)
                .message(message)
                .data(data)
                .build();
    }
}
