package com.summer.be.member.repository;

import com.summer.be.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByKakaoAccountId(String kakaoAccountId);
}
