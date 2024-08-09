package com.summer.be.member.service;

import com.summer.be.member.domain.EnglishLevel;
import com.summer.be.member.domain.Member;
import com.summer.be.member.domain.dto.LoginResponseDto;
import com.summer.be.member.repository.MemberRepository;
import com.summer.be.security.jwt.AuthTokens;
import com.summer.be.security.jwt.AuthTokensGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final EnglishLevel defaultLevel = EnglishLevel.BEGINNER;
    private final AuthTokensGenerator authTokensGenerator;

    @Transactional //카카오 계정 ID로 로그인하는데 없을 경우 생성
    public LoginResponseDto login(String kakaoAccountId) {
        Member member = memberRepository.findByKakaoAccountId(kakaoAccountId).orElse(null);

        if (member == null) {
            member = Member.builder()
                    .kakaoAccountId(kakaoAccountId) //kakaoAccountId로 Member 생성
                    .level(defaultLevel)
                    .build();

            memberRepository.save(member);
        }

        AuthTokens authTokens = authTokensGenerator.generate(kakaoAccountId);

        return LoginResponseDto.builder()
                .accessToken(authTokens.getAccessToken())
                .englishLevel(member.getLevel().toString())
                .build();
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }
}
