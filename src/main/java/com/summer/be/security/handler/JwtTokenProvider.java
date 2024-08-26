package com.summer.be.security.handler;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenProvider {
    private final RedisTemplate<String, String> redisTemplate;
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;
    private static final String AUTHORITIES_KEY = "auth";
    private static Key key;

    public JwtTokenProvider(@Value("${custom.jwt.secretKey}") String secretKey, RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String accessTokenGenerate(String subject, Date expiredAt) {
        return generateToken(subject, expiredAt);
    }

    public String refreshTokenGenerate(String subject, Date expiredAt) {
        String refreshToken = generateToken(null, expiredAt);
        redisTemplate.opsForValue().set(
                subject,
                refreshToken,
                expiredAt.getTime() - System.currentTimeMillis(),
                java.util.concurrent.TimeUnit.MILLISECONDS
        );

        return refreshToken;
    }

    private String generateToken(String subject, Date expiredAt) {
        try {
            return Jwts.builder()
                    .setSubject(subject) //카카오 uid
                    .claim(AUTHORITIES_KEY, "ROLE_USER")
                    .setExpiration(expiredAt)
                    .signWith(key, SIGNATURE_ALGORITHM)
                    .compact();
        } catch (Exception e) {
            log.info("Token generation failed", e);
            throw new RuntimeException("Token generation failed", e);
        }
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = getClaimsFormToken(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public static boolean isValidToken(String token) {
        try{
            Claims claims = getClaimsFormToken(token);

            log.info("expireTime : " + claims.getExpiration());
            log.info("loginId : " + claims.getSubject());

            return true;
        }
        catch (ExpiredJwtException expiredJwtException){
            log.error("Token Expired", expiredJwtException);
            return false;
        }
        catch (JwtException jwtException){
            log.error("Token Tampered", jwtException);
            return false;
        }
        catch (NullPointerException npe){
            log.error("Token is null", npe);
            return false;
        }
    }

    public static Claims getClaimsFormToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public static String getUserIdFromToken(String token) {
        Claims claims = getClaimsFormToken(token);
        return claims.getSubject();
    }
}
