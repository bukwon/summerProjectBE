package com.summer.be.member.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "client")
public class SocialProperties {
    private String key;
    private String redirectUri;

    // Getters and Setters
}