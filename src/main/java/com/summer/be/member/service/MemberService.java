package com.summer.be.member.service;

import com.summer.be.member.domain.EnglishLevel;
import com.summer.be.member.domain.Member;
import com.summer.be.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final EnglishLevel defaultLevel = EnglishLevel.BEGINNER;

    public Member login(String kakaoAccountId) {
        Member member = memberRepository.findByKakaoAccountId(kakaoAccountId)
                .orElseThrow(() -> new IllegalArgumentException("KaKaoAccountId not found"));

        return member;
    }

    @Transactional
    public Member signUp(String kakaoAccountId) {
        memberRepository.findByKakaoAccountId(kakaoAccountId)
                .ifPresent(member -> {
                    throw new IllegalArgumentException("KaKaoAccountId already exists");
                });

        Member member = Member.builder()
                .kakaoAccountId(kakaoAccountId) //kakaoAccountId로 Member 생성
                .level(defaultLevel)
                .build();

        memberRepository.save(member);

        return member;
    }

}
