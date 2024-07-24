package com.summer.be.openai.dto;

import lombok.Data;

import java.util.List;

@Data
public class OpenAIResponse {
    private List<Choice> choices;

    @Data
    public static class Choice {
        private Message message;
    }

    @Data
    public static class Message {
        private String role;
        private String content;
    }
}

// Response 용 DTO