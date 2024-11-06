package com.devb.estores.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.util.List;

@Configuration
@EnableCaching
@Slf4j
public class CacheInitializer {

    @Bean
    JCacheCacheManager cacheManager(List<CacheConfigurer<?,?>> cacheConfigurers) {
        log.info("Configuring CacheManger, {} cache configurations found", cacheConfigurers.size());
        CachingProvider provider = Caching.getCachingProvider();
        CacheManager manager = provider.getCacheManager();

        cacheConfigurers.forEach(cacheConfigurer -> {
            manager.createCache(cacheConfigurer.getCacheName(), cacheConfigurer.getEhCacheConfiguration());
        });

        return new JCacheCacheManager(manager);
    }
}
