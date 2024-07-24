package com.summer.be.member.domain.dto;

import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import static jakarta.persistence.EnumType.STRING;

@Data
@AllArgsConstructor
@Builder
public class KakaoDto {
    private Long id;
    private String nickname;
    //private String profileImg;
    private String email;
}