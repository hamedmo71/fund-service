package com.familyfund.infrastructure.persistence.member;

import com.familyfund.application.member.port.MemberRepository;
import com.familyfund.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByMemberCode(String memberCode);

    boolean existsByMemberCode(String memberCode);
}