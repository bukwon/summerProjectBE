package com.summer.be.member.domain.dto;

import lombok.Value;

@Value
public class RequestLoginDto
{
    String email;
    String password;
}
