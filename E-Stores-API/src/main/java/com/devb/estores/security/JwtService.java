package com.devb.estores.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
public class JwtService {

    private final String secret;
    private final long accessTokenExpirySeconds;
    private final long refreshTokenExpirySeconds;

    public JwtService(
            @Value("${myapp.jwt.secret}") String secret,
            @Value("${token.expiry.access.seconds}") long accessTokenExpirySeconds,
            @Value("${token.expiry.refresh.seconds}") long refreshTokenExpirySeconds) {
        this.secret = secret;
        this.accessTokenExpirySeconds = accessTokenExpirySeconds;
        this.refreshTokenExpirySeconds = refreshTokenExpirySeconds;
    }

    public static final String CLAIM_ROLES = "roles";
    public static final String CLAIM_BROWSER_NAME = "browser";
    public static final String CLAIM_SEC_CH_UA_PLATFORM = "secChUaPlatform";
    public static final String CLAIM_SEC_CH_UA_MOBILE = "secChUaMobile";
    public static final String CLAIM_JWT_ID = "jti";
    public static final String CLAIM_USER_AGENT = "userAgent";

    public Map<String, Object> generateClaims(List<String> roles, String browser, String secChUaPlatform,
                                              String secChUaMobile, String userAgent) {

        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_ROLES, (roles != null) ? roles : List.of());
        claims.put(CLAIM_BROWSER_NAME, (browser != null) ? browser : "");
        claims.put(CLAIM_SEC_CH_UA_PLATFORM, (secChUaPlatform != null) ? secChUaPlatform : "");
        claims.put(CLAIM_SEC_CH_UA_MOBILE, (secChUaMobile != null) ? secChUaMobile : "");
        claims.put(CLAIM_USER_AGENT, (userAgent != null) ? userAgent : "");

        return claims;
    }

    public Map<String, Object> setJwtId(Map<String, Object> claims, String tokenSessionId) {
        claims.put(CLAIM_JWT_ID, tokenSessionId);
        return claims;
    }

    public String generateAccessToken(String username, Map<String, Object> claims) {
        log.info("Generating Access Token...");
        return createJwtToken(claims, username, accessTokenExpirySeconds * 1000L);
    }

    public String generateRefreshToken(String username, Map<String, Object> claims) {
        log.info("Generating Refresh Token...");
        return createJwtToken(claims, username, refreshTokenExpirySeconds * 1000L);
    }

    private String createJwtToken(Map<String, Object> claims, String username, long expiryDuration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date((System.currentTimeMillis() + expiryDuration)))
                .signWith(getSignatureKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    private Key getSignatureKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    // parsing JWT

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignatureKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUsername(Claims claims) {
        return claims.getSubject();
    }

    public List<String> getUserRoles(Claims claims) {
        return claims.get(CLAIM_ROLES, List.class);
    }

    public Date getExpiry(Claims claims){
        return claims.getExpiration();
    }

    public Date getIssuedAt(Claims claims) {
        return claims.getIssuedAt();
    }

    public String getBrowserName(Claims claims) {
        return claims.get(CLAIM_BROWSER_NAME, String.class);
    }

    public String getSecChUaPlatform(Claims claims) {
        return claims.get(CLAIM_SEC_CH_UA_PLATFORM, String.class);
    }

    public String getSecChUaMobile(Claims claims) {
        return claims.get(CLAIM_SEC_CH_UA_MOBILE, String.class);
    }

    public String getJwtId(Claims claims) {
        return claims.get(CLAIM_JWT_ID, String.class);
    }

    public String getUserAgent(Claims claims) {
        return claims.get(CLAIM_USER_AGENT, String.class);
    }
}
