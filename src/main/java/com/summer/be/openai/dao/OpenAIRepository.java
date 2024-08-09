package com.summer.be.openai.dao;


import com.summer.be.openai.entity.OpenAI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpenAIRepository extends JpaRepository<OpenAI, Long> {
}
