package com.self.flipcart.security;

import java.util.Date;

public interface JwtService {

    String generateAccessToken(String username, String role);

    String generateRefreshToken(String username, String role);

    String extractUsername(String token);

    String extractUserRole(String token);

    Date extractExpiry(String token);

    Date extractIssuedAt(String refreshToken);
}
