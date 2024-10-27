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
import java.util.List;
import java.util.Map;


@Slf4j
@Service
public class JwtServiceImpl implements JwtService {

    @Value("${myapp.jwt.secret}")
    private String secret;

    @Value("${token.expiry.access.seconds}")
    private long accessTokenExpirySeconds;
    @Value("${token.expiry.refresh.seconds}")
    private long refreshTokenExpirySeconds;

    private static final String CLAIM_ROLES = "roles";
    private static final String CLAIM_BROWSER_NAME = "browser";
    private static final String CLAIM_SEC_CH_UA_PLATFORM = "secChUaPlatform";
    private static final String CLAIM_SEC_CH_UA_MOBILE = "secChUaMobile";
    private static final String CLAIM_TOKEN_SESSION_ID = "tsid";

    @Override
    public Map<String, Object> generateClaims(List<String> roles, String browser, String secChUaPlatform, String secChUaMobile, String tsid) {
        return Map.of(
                CLAIM_ROLES, roles,
                CLAIM_BROWSER_NAME, browser,
                CLAIM_SEC_CH_UA_PLATFORM, secChUaPlatform,
                CLAIM_SEC_CH_UA_MOBILE, secChUaMobile,
                CLAIM_TOKEN_SESSION_ID, tsid
        );
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
    public List getUserRoles(Claims claims) {
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

}
