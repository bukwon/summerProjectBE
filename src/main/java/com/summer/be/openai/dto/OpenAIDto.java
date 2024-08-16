package com.summer.be.openai.dto;

import com.google.gson.Gson;
import com.summer.be.openai.entity.OpenAI;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OpenAIDto {
    private String topic;
    private List<String> sentences;
    private List<String> vocabulary;

    public OpenAI toEntity() {
        Gson gson = new Gson();
        String sentencesJson = gson.toJson(sentences);
        String vocaJson = gson.toJson(vocabulary);

        OpenAI openAI = OpenAI.builder()
                .topic(topic)
                .sentences(sentencesJson)
                .vocabulary(vocaJson)
                .build();
        return openAI;
    }

    public static OpenAIDto fromEntity(OpenAI openAI) {
        Gson gson = new Gson();
        List<String> sentencesList = gson.fromJson(openAI.getSentences(), List.class);
        List<String> vocabularyList = gson.fromJson(openAI.getVocabulary(), List.class);

        return new OpenAIDto(
                openAI.getTopic(),
                sentencesList,
                vocabularyList
        );
    }
}
