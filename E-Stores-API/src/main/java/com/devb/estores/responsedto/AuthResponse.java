package com.devb.estores.responsedto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String userId;
    private String username;
    private List<String> roles;
    private boolean isAuthenticated;
    private long accessExpiration;
    private long refreshExpiration;
}
