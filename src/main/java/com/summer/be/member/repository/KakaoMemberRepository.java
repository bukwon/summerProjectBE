package com.summer.be.member.repository;

import com.summer.be.member.domain.KakaoMember;
import com.summer.be.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KakaoMemberRepository extends JpaRepository<KakaoMember, Long>{
    Optional<KakaoMember> findByEmail(String email);

}
