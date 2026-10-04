package com.familyfund.infrastructure.persistence.membership.adapter;

import com.familyfund.application.membership.port.MembershipRepository;
import com.familyfund.domain.membership.Membership;
import com.familyfund.infrastructure.persistence.membership.MembershipJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public class MembershipRepositoryAdapter implements MembershipRepository {

    private final MembershipJpaRepository jpaRepository;

    public MembershipRepositoryAdapter(MembershipJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Membership save(Membership membership) {
        return jpaRepository.save(membership);
    }

    @Override
    public Optional<Membership> findActiveMembershipByMemberId(Long memberId) {

        return jpaRepository.findActiveMembershipByMemberId(memberId);
    }

    @Override
    public boolean existsOverlappingMembership(Long memberId, LocalDate startDate, LocalDate endDate) {

        return jpaRepository.existsOverlappingMembership(memberId, startDate, endDate);
    }
}