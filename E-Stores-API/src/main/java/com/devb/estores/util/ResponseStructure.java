package com.devb.estores.util;

import org.springframework.stereotype.Component;

@Component
public class ResponseStructure<T> {
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

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> {
        private int status;
        private String message;
        private T data;

        public Builder<T> status(int status) {
            this.status = status;
            return this;
        }

        public Builder<T> message(String message) {
            this.message = message;
            return this;
        }

        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public ResponseStructure<T> build() {
            ResponseStructure<T> structure = new ResponseStructure<>();
            structure.status = this.status;
            structure.message = this.message;
            structure.data = this.data;
            return structure;
        }
    }

}
