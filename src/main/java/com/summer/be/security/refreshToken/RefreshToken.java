package com.summer.be.security.refreshToken;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor
@RedisHash(value = "refresh_token", timeToLive = 60 * 60 * 24 * 14) //7일간 유지
public class RefreshToken {

    @Id // import org.springframework.data.annotation.Id;
    @Column(name = "rt_key") //key는 refreshToken을 의미
    private String key;

    @Column(name = "rt_value") //value는 kakaoId를 의미
    private String value;

    @Builder
    public RefreshToken(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public RefreshToken updateKey(String token) {
        this.key = token;
        return this;
    }
}

