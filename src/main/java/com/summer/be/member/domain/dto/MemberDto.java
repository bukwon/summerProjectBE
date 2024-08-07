package com.summer.be.member.domain.dto;

import com.summer.be.member.domain.EnglishLevel;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.summer.be.member.domain.Member}
 */
@Value
public class MemberDto implements Serializable {
    @NotNull
    String kakaoAccountId;
    @NotNull
    EnglishLevel level;
}