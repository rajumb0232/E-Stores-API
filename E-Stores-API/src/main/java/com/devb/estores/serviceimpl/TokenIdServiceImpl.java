package com.devb.estores.serviceimpl;

import com.devb.estores.cache.CacheName;
import com.devb.estores.cache.CacheService;
import com.devb.estores.config.AppEnv;
import com.devb.estores.enums.TokenType;
import com.devb.estores.exceptions.InvalidJwtException;
import com.devb.estores.model.TokenIdentification;
import com.devb.estores.repository.TokenIdentificationRepo;
import com.devb.estores.service.TokenIdService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class TokenIdServiceImpl implements TokenIdService {

    private final TokenIdentificationRepo tokenIdRepo;
    private final CacheService cacheService;
    private final AppEnv appEnv;

    /**
     * Generates a new JTI and caches it to respective cache's,
     * the JTI is cached with the key created by combining username and deviceId.
     * Like, username + "." + deviceId
     *
     *  @param username username of the account.
     *  @param deviceId deviceId i.e, created for the client device identification.
     *  @param tokenType the token type to which the JTI is to be created.
     *  */
    @Override
    public String generateJwtId(String username, String deviceId, TokenType tokenType) {
        String jti = UUID.randomUUID().toString();
        String key = username + "." + deviceId;

        // Caching jti for token identification in future
        switch (tokenType) {
            case ACCESS -> {
                this.saveJtiForBackup(username, deviceId, tokenType, jti, LocalDateTime.now().plusSeconds(appEnv.getJwt().getAccessExpirationSeconds()));
                cacheService.doEntry(CacheName.ACCESS_TOKEN_CACHE, key, jti);
            }
            case REFRESH -> {
                this.saveJtiForBackup(username, deviceId, tokenType, jti, LocalDateTime.now().plusSeconds(appEnv.getJwt().getRefreshExpirationSeconds()));
                cacheService.doEntry(CacheName.REFRESH_TOKEN_CACHE, key, jti);
            }
        }

        return jti;
    }

    private void saveJtiForBackup(String username, String deviceId, TokenType tokenType, String jti, LocalDateTime expiration) {
        tokenIdRepo.save(TokenIdentification.builder()
                .username(username)
                .deviceId(deviceId)
                .tokenType(tokenType)
                .jti(jti)
                .expiration(expiration)
                .build());
    }

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

    @Override
    public void deleteJti(String username, String deviceId) {
        String key = username + "." + deviceId;
        tokenIdRepo.deleteByUsernameAndDeviceIdAndTokenType(username, deviceId, TokenType.ACCESS);
        tokenIdRepo.deleteByUsernameAndDeviceIdAndTokenType(username, deviceId, TokenType.ACCESS);
        cacheService.evictEntry(CacheName.ACCESS_TOKEN_CACHE, key);
        cacheService.evictEntry(CacheName.REFRESH_TOKEN_CACHE, key);
    }

    @Override
    @Transactional
    public void deleteAllOtherIds(String username, String currentDeviceId) {
        List<TokenIdentification> ids = tokenIdRepo.findAllByUsernameAndDeviceIdNot(username, currentDeviceId);

        if (ids.isEmpty()) {
            log.info("No tokens found for user '{}' on other devices; nothing to delete.", username);
            return;
        }

        this.evictCachesForListOfIds(ids);
        tokenIdRepo.deleteAll(ids);
    }

    @Override
    @Transactional
    public void deleteAllIds(String username) {
        List<TokenIdentification> ids = tokenIdRepo.findAllByUsername(username);
        this.evictCachesForListOfIds(ids);
        tokenIdRepo.deleteAll(ids);
    }

    private void evictCachesForListOfIds(List<TokenIdentification> ids) {
        ids.forEach(id -> {
            String key = id.getUsername() + "." + id.getDeviceId();
            switch (id.getTokenType()){
                case ACCESS -> cacheService.evictEntry(CacheName.ACCESS_TOKEN_CACHE, key);
                case REFRESH -> cacheService.evictEntry(CacheName.REFRESH_TOKEN_CACHE, key);
            }
            log.info("Evicted {} token from cache for key: {}", id.getTokenType().name(), key);
        });
    }
}
