package com.devb.estores.cache;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.jsr107.Eh107Configuration;

import javax.cache.configuration.Configuration;
import java.time.Duration;

@Getter
@Builder
@Slf4j
public class CacheConfigurer<K, V> {
    private String cacheName;
    private Class<K> keyType;
    private Class<V> valueType;
    private int heap;
    private int offHeap;
    private Duration ttl;

    public Configuration<K, V> getEhCacheConfiguration() {
        log.info("Configuring Cache with name: {}, heap: {}, offHeap: {}, TTL: {}", this.cacheName, this.heap, this.offHeap, this.ttl.toMinutes() + " Minutes");
        ResourcePoolsBuilder resourcePoolsBuilder = ResourcePoolsBuilder.newResourcePoolsBuilder()
                .heap(this.heap, EntryUnit.ENTRIES).offheap(this.offHeap > 0 ? this.offHeap : 10, MemoryUnit.MB);

        CacheConfiguration<K, V> cacheConfiguration = CacheConfigurationBuilder
                .newCacheConfigurationBuilder(
                        this.keyType,
                        this.valueType,
                        resourcePoolsBuilder)
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(this.ttl))
                .build();

        return Eh107Configuration.fromEhcacheCacheConfiguration(cacheConfiguration);
    }
}
