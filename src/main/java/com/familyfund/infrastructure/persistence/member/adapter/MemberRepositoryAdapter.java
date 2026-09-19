package com.familyfund.infrastructure.persistence.member.adapter;

import com.familyfund.application.member.port.MemberRepository;
import com.familyfund.domain.member.Member;
import com.familyfund.infrastructure.persistence.member.MemberJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MemberRepositoryAdapter implements MemberRepository {

    private final MemberJpaRepository memberJpaRepository;

    public MemberRepositoryAdapter(MemberJpaRepository memberJpaRepository) {
        this.memberJpaRepository = memberJpaRepository;
    }

    @Override
    public Member save(Member member) {
        return memberJpaRepository.save(member);
    }

    @Override
    public Optional<Member> findByMemberCode(String memberCode) {
        return memberJpaRepository.findByMemberCode(memberCode);
    }

    @Override
    public boolean existsByMemberCode(String memberCode) {
        return memberJpaRepository.existsByMemberCode(memberCode);
    }
}