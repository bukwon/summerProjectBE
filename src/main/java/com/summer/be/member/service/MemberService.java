package com.summer.be.member.service;

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

    public void login(String email, String password) {
        memberRepository.findByEmail(email)
                .filter(member -> member.getPassword().equals(password))
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
    }

    @Transactional
    public void signUp(String email, String password) {
        memberRepository.findByEmail(email)
                .ifPresent(member -> {
                    throw new IllegalArgumentException("Email already exists");
                });
        memberRepository.save(Member.builder()
                .email(email)
                .password(password)
                .build());
    }

    public boolean checkEmail(String email) {
        Optional<Member> existingMember = memberRepository.findByEmail(email);
        return existingMember.isPresent(); // 이메일이 존재하면 true, 아니면 false 반환
    }
}
