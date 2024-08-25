package com.summer.be.openai.dao;

import com.summer.be.openai.entity.Sentences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SentencesRepository extends JpaRepository<Sentences, Long> {
}
