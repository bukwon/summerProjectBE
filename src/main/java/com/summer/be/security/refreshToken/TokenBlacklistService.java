package com.summer.be.security.refreshToken;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class TokenBlacklistService {

    private final StringRedisTemplate redisTemplate;
    private final long tokenBlacklistTTL = 1000 * 60 * 60 * 24 * 14; // 14 days

    public TokenBlacklistService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void addToBlacklist(String token) {
        redisTemplate.opsForValue().set(token, "blacklisted", Duration.ofMillis(tokenBlacklistTTL));
    }

    public boolean isBlacklisted(String token) {
        return redisTemplate.hasKey(token);
    }
}