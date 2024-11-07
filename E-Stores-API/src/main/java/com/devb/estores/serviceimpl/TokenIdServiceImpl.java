package com.devb.estores.serviceimpl;

import com.devb.estores.cache.CacheName;
import com.devb.estores.cache.CacheService;
import com.devb.estores.enums.TokenType;
import com.devb.estores.exceptions.InvalidJwtException;
import com.devb.estores.model.TokenIdentification;
import com.devb.estores.repository.TokenIdentificationRepo;
import com.devb.estores.service.TokenIdService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TokenIdServiceImpl implements TokenIdService {

    private final TokenIdentificationRepo tokenIdRepo;
    private final CacheService cacheService;

    @Override
    public String getJti(String username, String deviceId, TokenType tokenType) {
        String cacheKey = username + "." + deviceId;
        String jti = this.getCachedJti(cacheKey, tokenType);

        if (jti == null)
            jti = this.fetchAndCacheJti(username, deviceId, tokenType);

        return jti;
    }

    private String fetchAndCacheJti(String username, String deviceId, TokenType tokenType) {
        TokenIdentification tokenIdentity = tokenIdRepo.findByUsernameAndDeviceIdAndTokenType(username, deviceId, tokenType)
                .orElseThrow(() -> new InvalidJwtException("Failed to identify token"));
        String jti = tokenIdentity.getJti();
        if (tokenType.equals(TokenType.ACCESS))
            cacheService.doEntry(CacheName.ACCESS_TOKEN_CACHE, username + "." + deviceId, jti);
        else
            cacheService.doEntry(CacheName.REFRESH_TOKEN_CACHE, username + "." + deviceId, jti);

        return jti;
    }

    private String getCachedJti(String cacheKey, TokenType tokenType) {
        if (tokenType.equals(TokenType.ACCESS))
            return cacheService.getEntry(CacheName.ACCESS_TOKEN_CACHE, cacheKey, String.class);
        else if (tokenType.equals(TokenType.REFRESH))
            return cacheService.getEntry(CacheName.REFRESH_TOKEN_CACHE, cacheKey, String.class);
        else return null;
    }
}
