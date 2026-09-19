package com.familyfund.application.member.port;

import com.familyfund.domain.member.Member;

import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);

    Optional<Member> findByMemberCode(String memberCode);

    boolean existsByMemberCode(String memberCode);
}