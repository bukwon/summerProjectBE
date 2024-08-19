package com.summer.be.member.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.io.Serializable;

@Value
public class TokenRequestDto implements Serializable {

    String accessToken;
    String refreshToken;
}
