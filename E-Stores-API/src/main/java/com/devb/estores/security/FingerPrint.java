package com.devb.estores.security;

import lombok.*;

@Getter
@Builder
public class FingerPrint {
    private String id;
    private String deviceId;
}
