package com.summer.be.security.refreshToken;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByKey(String key); //KakaoAccountId로 토큰 찾기
}

