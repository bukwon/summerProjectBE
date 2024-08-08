package com.summer.be.openai.dto;

import com.google.gson.Gson;
import com.summer.be.openai.entity.OpenAI;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OpenAIDto {
    private String topic;
    private List<String> sentences;

    public OpenAI toEntity() {
        Gson gson = new Gson();
        String sentencesJson = gson.toJson(sentences);

        OpenAI openAI = OpenAI.builder()
                .topic(topic)
                .sentences(sentencesJson)
                .build();
        return openAI;
    }
}
