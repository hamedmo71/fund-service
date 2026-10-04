package com.familyfund.infrastructure.persistence.membership.adapter;

import com.familyfund.domain.membership.MembershipFeePeriod;
import com.familyfund.domain.membership.MembershipFeeRule;
import com.familyfund.domain.shared.Money;
import com.familyfund.infrastructure.persistence.membership.MembershipFeeRuleJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MembershipFeeRuleRepositoryAdapterTest {

    private MembershipFeeRuleJpaRepository jpaRepository;
    private MembershipFeeRuleRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        jpaRepository = mock(MembershipFeeRuleJpaRepository.class);
        adapter = new MembershipFeeRuleRepositoryAdapter(jpaRepository);
    }

    @Test
    void shouldSaveMembershipFeeRule() {

        MembershipFeeRule rule = createRule();

        when(jpaRepository.save(rule)).thenReturn(rule);

        MembershipFeeRule result = adapter.save(rule);

        assertSame(rule, result);

        verify(jpaRepository).save(rule);
    }

    @Test
    void shouldFindEffectiveMembershipFeeRule() {

        MembershipFeeRule rule = createRule();

        LocalDate date = LocalDate.of(2026, 6, 1);

        when(jpaRepository.findEffectiveRule(date)).thenReturn(Optional.of(rule));

        Optional<MembershipFeeRule> result = adapter.findEffectiveRule(date);

        assertTrue(result.isPresent());
        assertSame(rule, result.get());

        verify(jpaRepository).findEffectiveRule(date);
    }

    @Test
    void shouldReturnEmptyWhenNoEffectiveMembershipFeeRuleExists() {

        LocalDate date = LocalDate.of(2026, 6, 1);

        when(jpaRepository.findEffectiveRule(date)).thenReturn(Optional.empty());

        Optional<MembershipFeeRule> result = adapter.findEffectiveRule(date);

        assertTrue(result.isEmpty());

        verify(jpaRepository).findEffectiveRule(date);
    }

    @Test
    void shouldCheckWhetherOverlappingRuleExists() {

        LocalDate effectiveFrom = LocalDate.of(2026, 3, 21);

        LocalDate effectiveTo = LocalDate.of(2026, 9, 22);

        when(jpaRepository.existsOverlappingRule(effectiveFrom, effectiveTo)).thenReturn(true);

        boolean result = adapter.existsOverlappingRule(effectiveFrom, effectiveTo);

        assertTrue(result);

        verify(jpaRepository).existsOverlappingRule(effectiveFrom, effectiveTo);
    }

    @Test
    void shouldReturnFalseWhenNoOverlappingRuleExists() {

        LocalDate effectiveFrom = LocalDate.of(2026, 9, 23);

        LocalDate effectiveTo = LocalDate.of(2026, 12, 31);

        when(jpaRepository.existsOverlappingRule(effectiveFrom, effectiveTo)).thenReturn(false);

        boolean result = adapter.existsOverlappingRule(effectiveFrom, effectiveTo);

        assertFalse(result);

        verify(jpaRepository).existsOverlappingRule(effectiveFrom, effectiveTo);
    }

    @Test
    void shouldCheckOverlapForOpenEndedRule() {

        LocalDate effectiveFrom = LocalDate.of(2026, 9, 23);

        when(jpaRepository.existsOverlappingRule(effectiveFrom, null)).thenReturn(false);

        boolean result = adapter.existsOverlappingRule(effectiveFrom, null);

        assertFalse(result);

        verify(jpaRepository).existsOverlappingRule(effectiveFrom, null);
    }

    private MembershipFeeRule createRule() {

        return new MembershipFeeRule(LocalDate.of(2026, 3, 21), LocalDate.of(2026, 9, 22), Money.of(1_000_000L), MembershipFeePeriod.MONTHLY);
    }
}