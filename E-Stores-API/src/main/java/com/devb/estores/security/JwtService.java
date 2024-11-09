package com.devb.estores.security;

import com.devb.estores.config.AppEnv;
import com.devb.estores.exceptions.InvalidJwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;


@Slf4j
@Service
@AllArgsConstructor
public class JwtService {

    private final AppEnv appEnv;

    public String generateAccessToken(TokenPayload tokenPayload) {
        log.info("Generating Access Token...");
        return createJwtToken(tokenPayload);
    }

    public String generateRefreshToken(TokenPayload tokenPayload) {
        log.info("Generating Refresh Token...");
        return createJwtToken(tokenPayload);
    }

    private String createJwtToken(TokenPayload tokenPayload) {
        return Jwts.builder()
                .setClaims(tokenPayload.getClaims())
                .setSubject(tokenPayload.getSubject())
                .setIssuedAt(tokenPayload.getIssuedAt())
                .setExpiration(tokenPayload.getExpiration())
                .signWith(getSignatureKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    private Key getSignatureKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(appEnv.getJwt().getSecret()));
    }

    // parsing JWT

    public Claims extractClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignatureKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException e){
            // Returning null to handle as per requirement
            return null;
        }
    }

    public Claims extractClaimsOrThrow(String token) throws InvalidJwtException {
        Claims claims = extractClaims(token);
        if(claims == null)
            throw new InvalidJwtException("Failed to parse the token");
        return claims;
    }

    public String getUsername(Claims claims) {
        return claims.getSubject();
    }

    public List<String> getUserRoles(Claims claims) {
        return claims.get(TokenPayload.CLAIM_ROLES, List.class);
    }

    public Date getExpiry(Claims claims){
        return claims.getExpiration();
    }

    public Date getIssuedAt(Claims claims) {
        return claims.getIssuedAt();
    }

    public String getBrowserName(Claims claims) {
        return claims.get(TokenPayload.CLAIM_BROWSER_NAME, String.class);
    }

    public String getSecChUaPlatform(Claims claims) {
        return claims.get(TokenPayload.CLAIM_SEC_CH_UA_PLATFORM, String.class);
    }

    public String getSecChUaMobile(Claims claims) {
        return claims.get(TokenPayload.CLAIM_SEC_CH_UA_MOBILE, String.class);
    }

    public String getJwtId(Claims claims) {
        return claims.get(TokenPayload.CLAIM_JWT_ID, String.class);
    }

    public String getUserAgent(Claims claims) {
        return claims.get(TokenPayload.CLAIM_USER_AGENT, String.class);
    }
}
