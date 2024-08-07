package com.summer.be.member.domain.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class KakaoAccountIdDto {
    String kakaoAccountId;
}
