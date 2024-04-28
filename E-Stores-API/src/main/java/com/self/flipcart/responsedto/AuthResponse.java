package com.self.flipcart.responsedto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String userId;
    private String username;
    private String role;
    private boolean isAuthenticated;
    private long accessExpiration;
    private long refreshExpiration;
}
