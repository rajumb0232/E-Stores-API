package com.devb.estores.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.lang.Maps;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;


@Slf4j
@Service
public class JwtServiceImpl implements JwtService {

    @Value("${myapp.jwt.secret}")
    private String secret;

    @Value("${token.expiry.access.seconds}")
    private long accessTokenExpirySeconds;
    @Value("${token.expiry.refresh.seconds}")
    private long refreshTokenExpirySeconds;

    public static final String CLAIM_ROLES = "roles";

    public String generateAccessToken(String username, String roles) {
        log.info("Generating Access Token...");
        return createJwtToken(roles, username, accessTokenExpirySeconds * 1000L);
    }

    public String generateRefreshToken(String username, String roles) {
        log.info("Generating Refresh Token...");
        return createJwtToken(roles, username, refreshTokenExpirySeconds * 1000L);
    }

    private String createJwtToken(String roles, String username, long expiryDuration) {
        return Jwts.builder()
                .setClaims(Maps.of(CLAIM_ROLES, roles).build())
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

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public String extractUserRoles(String token) {
        return parseClaims(token).get(CLAIM_ROLES, String.class);
    }

    @Override
    public Date extractExpiry(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    @Override
    public Date extractIssuedAt(String token) {
        return extractClaim(token, Claims::getIssuedAt);
    }

    private <R> R extractClaim(String token, Function<Claims, R> claimResolver) {
        return claimResolver.apply(parseClaims(token));
    }

    private Claims parseClaims(String token) {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignatureKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
    }


}
