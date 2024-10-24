package com.devb.estores.util;

import lombok.Builder;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Builder
public class SimpleResponseStructure {
    private int status;
    private String message;

    public static SimpleResponseStructure create(int status, String message) {
        return SimpleResponseStructure.builder()
                .status(status)
                .message(message)
                .build();
    }

}
