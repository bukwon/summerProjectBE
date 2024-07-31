package com.summer.be.member.service;

import com.summer.be.member.domain.Member;
import com.summer.be.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member login(String email, String password) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return member;
    }

    @Transactional
    public void signUp(String email, String nickname, String password) {
        memberRepository.findByEmail(email)
                .ifPresent(member -> {
                    throw new IllegalArgumentException("Email already exists");
                });
        memberRepository.save(Member.builder()
                .email(email)
                .nickname(nickname)
                .password(passwordEncoder.encode(password))
                .build());
    }

    public boolean checkEmail(String email) {
        Optional<Member> existingMember = memberRepository.findByEmail(email);
        return existingMember.isPresent(); // 이메일이 존재하면 true, 아니면 false 반환
    }
}
