package com.summer.be.openai.dto;

import com.openai.openai.entity.Learnings;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LearningsDto {
    private String topic;

    public Learnings toEntity() {
        Learnings learnings = Learnings.builder()
                .topic(topic)
                .status(0)
                .build();
        return learnings;
    }
}
