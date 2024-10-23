package com.devb.estores.util;

import lombok.Builder;
import org.springframework.stereotype.Component;

@Component
@Builder
public class SimpleResponseStructure {
    private int status;
    private String message;

    public int getStatus() {
        return status;
    }

    public SimpleResponseStructure setStatus(int status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public SimpleResponseStructure setMessage(String message) {
        this.message = message;
        return this;
    }

}
