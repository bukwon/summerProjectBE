package com.summer.be.openai.dto;

import com.google.gson.Gson;
import com.summer.be.openai.entity.Learnings;
import com.summer.be.openai.entity.Vocabulary;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VocabularyDto {
    private List<String> vocabulary;
    private Learnings learnings;

    public Vocabulary toEntity() {
        Gson gson = new Gson();
        String vocaJson = gson.toJson(vocabulary);

        Vocabulary vocabulary = Vocabulary.builder()
                .vocabulary(vocaJson)
                .learnings(learnings)
                .status(0)
                .build();
        return vocabulary;
    }
}
