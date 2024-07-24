package com.summer.be.openai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpenAIRequest {
    private String model;
    private List<Message> messages;
}
// Request 용 DTO