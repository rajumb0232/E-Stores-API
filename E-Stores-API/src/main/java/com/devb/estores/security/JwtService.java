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


@Slf4j
@Service
public class JwtService {

    private final String secret;

    public JwtService(
            @Value("${myapp.jwt.secret}") String secret) {
        this.secret = secret;
    }

    public String generateAccessToken(JwtModel jwtModel) {
        log.info("Generating Access Token...");
        return createJwtToken(jwtModel);
    }

    public String generateRefreshToken(JwtModel jwtModel) {
        log.info("Generating Refresh Token...");
        return createJwtToken(jwtModel);
    }

    private String createJwtToken(JwtModel jwtModel) {
        return Jwts.builder()
                .setClaims(jwtModel.getClaims())
                .setSubject(jwtModel.getSubject())
                .setIssuedAt(jwtModel.getIssuedAt())
                .setExpiration(jwtModel.getExpiration())
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
        return claims.get(JwtModel.CLAIM_ROLES, List.class);
    }

    public Date getExpiry(Claims claims){
        return claims.getExpiration();
    }

    public Date getIssuedAt(Claims claims) {
        return claims.getIssuedAt();
    }

    public String getBrowserName(Claims claims) {
        return claims.get(JwtModel.CLAIM_BROWSER_NAME, String.class);
    }

    public String getSecChUaPlatform(Claims claims) {
        return claims.get(JwtModel.CLAIM_SEC_CH_UA_PLATFORM, String.class);
    }

    public String getSecChUaMobile(Claims claims) {
        return claims.get(JwtModel.CLAIM_SEC_CH_UA_MOBILE, String.class);
    }

    public String getJwtId(Claims claims) {
        return claims.get(JwtModel.CLAIM_JWT_ID, String.class);
    }

    public String getUserAgent(Claims claims) {
        return claims.get(JwtModel.CLAIM_USER_AGENT, String.class);
    }
}
