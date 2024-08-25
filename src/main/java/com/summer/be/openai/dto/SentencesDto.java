package com.summer.be.openai.dto;

import com.google.gson.Gson;
import com.summer.be.openai.entity.Learnings;
import com.summer.be.openai.entity.Sentences;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SentencesDto {
    private List<String> sentences;
    private Learnings learnings;

    public Sentences toEntity() {
        Gson gson = new Gson();
        String sentencesJson = gson.toJson(sentences);

        Sentences saveSentences = Sentences.builder()
                .sentences(sentencesJson)
                .learnings(learnings)
                .status(0)
                .build();
        return saveSentences;
    }
}
