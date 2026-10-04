package com.familyfund.application.membership;

import com.familyfund.application.membership.exception.MembershipFeeRuleOverlapException;
import com.familyfund.application.membership.port.MembershipFeeRuleRepository;
import com.familyfund.domain.membership.MembershipFeeRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Optional;

public class MembershipFeeRuleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MembershipFeeRuleService.class);

    private final MembershipFeeRuleRepository repository;

    public MembershipFeeRuleService(MembershipFeeRuleRepository repository) {

        this.repository = repository;
    }

    public MembershipFeeRule createRule(MembershipFeeRule rule) {

        LOGGER.debug("Creating membership fee rule: " + "effectiveFrom={}, effectiveTo={}, amount={}, period={}", rule.getEffectiveFrom(), rule.getEffectiveTo(), rule.getAmount(), rule.getPeriod());

        if (repository.existsOverlappingRule(rule.getEffectiveFrom(), rule.getEffectiveTo())) {

            LOGGER.warn("Membership fee rule creation rejected " + "due to overlapping rule: " + "effectiveFrom={}, effectiveTo={}", rule.getEffectiveFrom(), rule.getEffectiveTo());

            throw new MembershipFeeRuleOverlapException();
        }

        MembershipFeeRule savedRule = repository.save(rule);

        LOGGER.info("Membership fee rule created successfully: " + "effectiveFrom={}, effectiveTo={}, amount={}, period={}", savedRule.getEffectiveFrom(), savedRule.getEffectiveTo(), savedRule.getAmount(), savedRule.getPeriod());

        return savedRule;
    }

    public Optional<MembershipFeeRule> findEffectiveRule(LocalDate date) {

        LOGGER.debug("Searching for effective membership fee rule " + "on date={}", date);

        Optional<MembershipFeeRule> result = repository.findEffectiveRule(date);

        if (result.isPresent()) {

            MembershipFeeRule rule = result.get();

            LOGGER.debug("Effective membership fee rule found: " + "effectiveFrom={}, effectiveTo={}, " + "amount={}, period={}", rule.getEffectiveFrom(), rule.getEffectiveTo(), rule.getAmount(), rule.getPeriod());

        } else {

            LOGGER.debug("No effective membership fee rule found " + "for date={}", date);
        }

        return result;
    }
}