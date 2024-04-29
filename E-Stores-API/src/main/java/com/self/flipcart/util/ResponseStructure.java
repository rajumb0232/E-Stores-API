package com.self.flipcart.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseStructure<T> extends Structure<T> {
    private int status;
    private String message;
    private T data;

    public int getStatus() {
        return status;
    }

    public ResponseStructure<T> setStatus(int status) {
       this.status = status;
       return this;
    }

    public String getMessage() {
        return message;
    }

    public ResponseStructure<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getData() {
        return data;
    }

    public ResponseStructure<T> setData(T data) {
        this.data = data;
        return this;
    }
}
