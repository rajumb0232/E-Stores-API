package com.self.flipcart.util;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class ErrorStructure<T> {
    private int status;
    private String message;
    private T rootCause;

    public int getStatus() {
        return status;
    }

    public ErrorStructure<T> setStatus(int status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ErrorStructure<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getRootCause() {
        return rootCause;
    }

    public ErrorStructure<T> setRootCause(T rootCause) {
        this.rootCause = rootCause;
        return this;
    }
}
