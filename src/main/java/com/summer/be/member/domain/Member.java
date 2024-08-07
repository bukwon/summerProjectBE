package com.summer.be.member.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String kakaoAccountId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EnglishLevel level;

    @Builder
    public Member(String kakaoAccountId, EnglishLevel level) {
        this.kakaoAccountId = kakaoAccountId;
        this.level = level;
    }

    public void changeLevel(EnglishLevel level) {
        this.level = level;
    }
}

    /*
    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Builder
    public Member(String nickname, String email, String password) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPassword());
    }
    */