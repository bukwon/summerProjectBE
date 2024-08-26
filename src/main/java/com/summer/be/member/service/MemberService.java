package com.summer.be.member.service;

import com.summer.be.member.domain.Authority;
import com.summer.be.member.domain.EnglishLevel;
import com.summer.be.member.domain.Member;
import com.summer.be.member.domain.dto.LoginResponseDto;
import com.summer.be.member.domain.dto.TokenRequestAndResponseDto;
import com.summer.be.member.repository.MemberRepository;
import com.summer.be.security.handler.JwtTokenProvider;
import com.summer.be.security.jwt.AuthTokens;
import com.summer.be.security.jwt.AuthTokensGenerator;
import com.summer.be.security.refreshToken.RefreshToken;
import com.summer.be.security.refreshToken.RefreshTokenRepository;
import com.summer.be.security.refreshToken.TokenBlacklistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final EnglishLevel defaultLevel = EnglishLevel.BEGINNER;
    private final AuthTokensGenerator authTokensGenerator;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenBlacklistService tokenBlacklistService;

    @Transactional //카카오 계정 ID로 로그인하는데 없을 경우 생성
    public LoginResponseDto login(String kakaoAccountId) {
        Member member = memberRepository.findByKakaoAccountId(kakaoAccountId).orElse(null);

        if (member == null) {
            member = Member.builder()
                    .kakaoAccountId(kakaoAccountId) //kakaoAccountId로 Member 생성
                    .level(defaultLevel)
                    .authority(Authority.ROLE_USER)
                    .build();

            memberRepository.save(member);
        }

        AuthTokens authTokens = authTokensGenerator.generate(kakaoAccountId);

        RefreshToken refreshToken = RefreshToken.builder()
                .key(authTokens.getRefreshToken())
                .value(kakaoAccountId)
                .build();

        refreshTokenRepository.save(refreshToken);

        return LoginResponseDto.builder()
                .accessToken(authTokens.getAccessToken())
                .refreshToken(authTokens.getRefreshToken())
                .englishLevel(member.getLevel().toString())
                .build();
    }

    @Transactional
    public AuthTokens reissue(TokenRequestAndResponseDto tokenRequestDto) {
        // 1. Access Token 에서 Member ID 가져오기
        Authentication authentication = jwtTokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 2. refreshToken이 blacklist에 있는지 확인
        if (tokenBlacklistService.isBlacklisted(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("블랙리스트에 등록된 토큰입니다.");
        }

        // 3. 저장소에서 refreshToken 을 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(tokenRequestDto.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("등록되지 않은 Refresh Token 입니다."));

        // 4. Refresh Token과 Access Token이 일치하는 유저의 것인지 검사
        if (!refreshToken.getValue().equals(authentication.getName())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        AuthTokens authTokens = authTokensGenerator.generate(authentication.getName());

        // 6. 기존 Refresh Token 블랙리스트에 추가
        tokenBlacklistService.addToBlacklist(tokenRequestDto.getRefreshToken());

        // 7. 저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateKey(authTokens.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // 토큰 발급
        return authTokens;
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }
}
