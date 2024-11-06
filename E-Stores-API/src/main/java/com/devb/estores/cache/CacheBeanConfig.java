package com.devb.estores.cache;

import com.devb.estores.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CacheBeanConfig {

    private <K, V> CacheConfigurer<K, V> configure(String cacheName, Class<K> keyType, Class<V> valueType, int heap, int offHeap, Duration ttl) {
        return CacheConfigurer.<K, V>builder()
                .cacheName(cacheName)
                .keyType(keyType)
                .valueType(valueType)
                .heap(heap)
                .offHeap(offHeap)
                .ttl(ttl)
                .build();
    }

    @Bean
    CacheConfigurer<String, Integer> OtpCache() {
        return this.configure(CacheName.OTP_CACHE, String.class, Integer.class, 10000, 1, Duration.ofMinutes(5));
    }

    @Bean
    CacheConfigurer<String, User> userCache() {
        return this.configure(CacheName.USER_CACHE, String.class, User.class, 20000, 10, Duration.ofDays(1));
    }
}
