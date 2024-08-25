package com.summer.be.openai.dto;

import com.google.gson.Gson;
import com.openai.openai.entity.Vocabulary;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VocabularyDto {
    private List<String> vocabulary;

    public Vocabulary toEntity() {
        Gson gson = new Gson();
        String vocaJson = gson.toJson(vocabulary);

        Vocabulary vocabulary = Vocabulary.builder()
                .vocabulary(vocaJson)
                .status(0)
                .build();
        return vocabulary;
    }
}
