package ru.otus.hw.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
@Configuration
@EnableCaching
public class CacheConfig {

    @Value("${cache.rooms.ttl-minutes}")
    private int roomsTtl;

    @Value("${cache.rooms.max-size}")
    private int roomsMaxSize;

    @Value("${cache.acl.ttl-minutes}")
    private int aclTtl;

    @Value("${cache.acl.max-size}")
    private int aclMaxSize;

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager manager = new SimpleCacheManager();

        CaffeineCache roomsCache = new CaffeineCache("rooms",
                Caffeine.newBuilder()
                        .expireAfterWrite(roomsTtl, TimeUnit.MINUTES)
                        .maximumSize(roomsMaxSize)
                        .build());

        CaffeineCache aclCache = new CaffeineCache("aclCache",
                Caffeine.newBuilder()
                        .expireAfterWrite(aclTtl, TimeUnit.MINUTES)
                        .maximumSize(aclMaxSize)
                        .build());

        manager.setCaches(Arrays.asList(roomsCache, aclCache));
        return manager;
    }
}