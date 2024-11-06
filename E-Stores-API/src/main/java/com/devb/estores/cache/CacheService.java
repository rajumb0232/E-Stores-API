package com.devb.estores.cache;

import lombok.AllArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CacheService {

    private final CacheManager cacheManager;

    public <K, V> boolean doEntry(String cacheName,  K key, V value) {
        Cache cache = cacheManager.getCache(cacheName);
        if(cache != null) {
            cache.put(key, value);
            return true;
        } else return false;
    }

    public <K, V> V getEntry(String cacheName, K key, Class<V> type) {
        Cache cache = cacheManager.getCache(cacheName);
        if(cache != null)
            return cache.get(key, type);
        else return null;
    }

    public <K> boolean evictEntry(String cacheName, K key) {
        Cache cache = cacheManager.getCache(cacheName);
        if(cache != null) {
            cache.evict(key);
            return true;
        } else return false;
    }

    public boolean clearCache(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.clear();
            return true;
        }
        return false;
    }
}
