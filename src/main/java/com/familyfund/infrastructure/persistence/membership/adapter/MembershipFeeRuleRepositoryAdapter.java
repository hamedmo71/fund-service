package com.familyfund.infrastructure.persistence.membership.adapter;

import com.familyfund.application.membership.port.MembershipFeeRuleRepository;
import com.familyfund.domain.membership.MembershipFeeRule;
import com.familyfund.infrastructure.persistence.membership.MembershipFeeRuleJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public class MembershipFeeRuleRepositoryAdapter implements MembershipFeeRuleRepository {

    private final MembershipFeeRuleJpaRepository jpaRepository;

    public MembershipFeeRuleRepositoryAdapter(MembershipFeeRuleJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public MembershipFeeRule save(MembershipFeeRule rule) {
        return jpaRepository.save(rule);
    }

    @Override
    public Optional<MembershipFeeRule> findEffectiveRule(LocalDate date) {
        return jpaRepository.findEffectiveRule(date);
    }

    @Override
    public boolean existsOverlappingRule(LocalDate effectiveFrom, LocalDate effectiveTo) {
        return jpaRepository.existsOverlappingRule(effectiveFrom, effectiveTo);
    }
}