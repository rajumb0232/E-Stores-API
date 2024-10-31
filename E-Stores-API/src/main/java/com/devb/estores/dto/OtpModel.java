package com.devb.estores.dto;

import lombok.*;

@Getter
@Builder
public class OtpModel {
    private String email;
    private int otp;
}
