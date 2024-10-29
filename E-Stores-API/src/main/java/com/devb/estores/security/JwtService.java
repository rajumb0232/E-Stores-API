package com.devb.estores.security;

import io.jsonwebtoken.Claims;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface JwtService {

    Map<String, Object> generateClaims(List<String> roles, String browser, String secChUaPlatform, String secChUaMobile, String userAgent);

    Map<String, Object> setJwtId(Map<String, Object> claims, String tokenSessionId);

    String generateAccessToken(String username, Map<String, Object> claims);

    String generateRefreshToken(String username, Map<String, Object> claims);

    Claims extractClaims(String token);

    String getUsername(Claims claims);

    List<String> getUserRoles(Claims claims);

    Date getExpiry(Claims claims);

    Date getIssuedAt(Claims claims);

    String getBrowserName(Claims claims);

    String getSecChUaPlatform(Claims claims);

    String getSecChUaMobile(Claims claims);

    String getJwtId(Claims claims);

    String getUserAgent(Claims claims);
}
