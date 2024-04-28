package com.self.flipcart.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

@Slf4j
public class CacheStore<T> {

    private Cache<String, T> cache;

    public CacheStore(Duration expiry) {
        this.cache = CacheBuilder.newBuilder()
                .expireAfterWrite(expiry)
                .concurrencyLevel(Runtime.getRuntime().availableProcessors()).build();
    }

    public T get(String key){
        return cache.getIfPresent(key);
    }

    public void add(String key, T value){
        cache.put(key, value);
        log.info("Cache added with the key: "+key);
    }

    public void remove(String key){
        cache.invalidate(key);
    }
}
