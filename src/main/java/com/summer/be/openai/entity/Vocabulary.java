package com.summer.be.openai.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Vocabulary")
@Getter
@Builder
public class Vocabulary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "English_vocabulary", columnDefinition = "LONGTEXT")
    private String vocabulary;

    @Column(name = "Status")
    private Integer status;

    @OneToOne
    @JoinColumn(name = "learnings_id")
    private Learnings learnings;
}
