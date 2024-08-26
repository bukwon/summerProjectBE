package com.summer.be.member.domain.dto;

import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

@Value
public class TokenRequestAndResponseDto implements Serializable {

    String accessToken;
    String refreshToken;

    @Builder
    public TokenRequestAndResponseDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
