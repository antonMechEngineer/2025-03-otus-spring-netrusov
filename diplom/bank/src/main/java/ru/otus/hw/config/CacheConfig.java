package ru.otus.hw.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
@Configuration
@EnableCaching
public class CacheConfig {

    @Value("${cache.acl.ttl-minutes}")
    private int aclTtl;

    @Value("${cache.acl.max-size}")
    private int aclMaxSize;

    @Bean
    public CacheManager cacheManager() {
        var manager = new SimpleCacheManager();
        var aclCache = new CaffeineCache("aclCache",
                Caffeine.newBuilder()
                        .expireAfterWrite(aclTtl, TimeUnit.MINUTES)
                        .maximumSize(aclMaxSize)
                        .build());
        manager.setCaches(List.of(aclCache));
        return manager;
    }
}