package com.summer.be.member.repository;

import com.summer.be.member.domain.KakaoMember;
import com.summer.be.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KakaoMemberRepository extends JpaRepository<KakaoMember, Long>{

}
