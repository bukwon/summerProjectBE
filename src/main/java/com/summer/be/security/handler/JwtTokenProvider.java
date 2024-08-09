package com.summer.be.security.handler;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;
    private final Key key;

    public JwtTokenProvider(@Value("${custom.jwt.secretKey}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String accessTokenGenerate(String subject, Date expiredAt) {
        return generateToken(subject, expiredAt);
    }

    public String refreshTokenGenerate(Date expiredAt) {
        return generateToken(null, expiredAt);
    }

    private String generateToken(String subject, Date expiredAt) {
        try {
            return Jwts.builder()
                    .setSubject(subject) //카카오 uid
                    .setExpiration(expiredAt)
                    .signWith(key, SIGNATURE_ALGORITHM)
                    .compact();
        } catch (Exception e) {
            log.info("Token generation failed", e);
            throw new RuntimeException("Token generation failed", e);
        }
    }
}
