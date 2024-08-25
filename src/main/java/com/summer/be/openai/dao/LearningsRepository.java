package com.summer.be.openai.dao;


import com.summer.be.openai.entity.Learnings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LearningsRepository extends JpaRepository<Learnings, Long> {
}
