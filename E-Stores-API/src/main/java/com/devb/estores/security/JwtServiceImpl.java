package com.devb.estores.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
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
public class JwtServiceImpl implements JwtService {

    private final String secret;
    private final long accessTokenExpirySeconds;
    private final long refreshTokenExpirySeconds;

    public JwtServiceImpl(
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
    public static final String CLAIM_TOKEN_SESSION_ID = "tsid";
    public static final String CLAIM_USER_AGENT = "userAgent";

    @Override
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

    @Override
    public Map<String, Object> setTokenSessionId(Map<String, Object> claims, String tokenSessionId) {
        claims.put(CLAIM_TOKEN_SESSION_ID, tokenSessionId);
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

    @Override
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

    @Override
    public List<String> getUserRoles(Claims claims) {
        return claims.get(CLAIM_ROLES, List.class);
    }

    @Override
    public Date getExpiry(Claims claims){
        return claims.getExpiration();
    }

    @Override
    public Date getIssuedAt(Claims claims) {
        return claims.getIssuedAt();
    }

    @Override
    public String getBrowserName(Claims claims) {
        return claims.get(CLAIM_BROWSER_NAME, String.class);
    }

    @Override
    public String getSecChUaPlatform(Claims claims) {
        return claims.get(CLAIM_SEC_CH_UA_PLATFORM, String.class);
    }

    @Override
    public String getSecChUaMobile(Claims claims) {
        return claims.get(CLAIM_SEC_CH_UA_MOBILE, String.class);
    }

    @Override
    public String getTsid(Claims claims) {
        return claims.get(CLAIM_TOKEN_SESSION_ID, String.class);
    }

    @Override
    public String getUserAgent(Claims claims) {
        return claims.get(CLAIM_USER_AGENT, String.class);
    }

}
