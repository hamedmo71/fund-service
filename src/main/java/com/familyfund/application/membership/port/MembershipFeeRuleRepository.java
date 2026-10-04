package com.familyfund.application.membership.port;

import com.familyfund.domain.membership.MembershipFeeRule;

import java.time.LocalDate;
import java.util.Optional;

public interface MembershipFeeRuleRepository {

    MembershipFeeRule save(MembershipFeeRule rule);

    Optional<MembershipFeeRule> findEffectiveRule(LocalDate date);

    boolean existsOverlappingRule(LocalDate effectiveFrom, LocalDate effectiveTo);
}