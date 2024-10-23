package com.devb.estores.util;

import lombok.Builder;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
@Builder
public class SimpleResponseStructure {
    private int status;
    private String message;

    public SimpleResponseStructure setStatus(int status) {
        this.status = status;
        return this;
    }

    public SimpleResponseStructure setMessage(String message) {
        this.message = message;
        return this;
    }

    public static SimpleResponseStructure create(int status, String message) {
        return SimpleResponseStructure.builder()
                .status(status)
                .message(message)
                .build();
    }

}
