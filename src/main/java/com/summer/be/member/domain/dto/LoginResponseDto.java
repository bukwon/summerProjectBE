package com.summer.be.member.domain.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LoginResponseDto {
    String englishLevel;
    String accessToken;

    @Builder
    public LoginResponseDto(String englishLevel, String accessToken) {
        this.englishLevel = englishLevel;
        this.accessToken = accessToken;
    }
}
