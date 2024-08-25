package com.summer.be.openai.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Entity
@AllArgsConstructor
@Table(name = "Sentences")
@Builder
@Getter
public class Sentences {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "English_sentence", columnDefinition = "LONGTEXT")
    private String sentences;

    @Column(name = "Status")
    private Integer status;

    @OneToOne
    @JoinColumn(name = "learnings_id")
    private Learnings learnings;
}
