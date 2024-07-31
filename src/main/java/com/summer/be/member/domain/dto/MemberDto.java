package com.summer.be.member.domain.dto;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.summer.be.member.domain.Member}
 */
@Value
public class MemberDto implements Serializable {
    String email;
    String nickname;

    String password;
}