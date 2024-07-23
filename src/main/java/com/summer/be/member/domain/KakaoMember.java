package com.summer.be.member.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class KakaoMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @Column(nullable = false)
    private Long id;
    @Column(nullable = false)
    private String nickname;
    @Column(nullable = false)
    private String profileImg;
    @Column(nullable = false)
    private String email;

    @Builder
    public KakaoMember(Long id, String nickname, String profileImg, String email) {
        this.id = id;
        this.nickname = nickname;
        this.profileImg = profileImg;
        this.email = email;
    }
}
