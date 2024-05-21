package com.devb.estores.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OtpModel {
    private String email;
    private int otp;
}
