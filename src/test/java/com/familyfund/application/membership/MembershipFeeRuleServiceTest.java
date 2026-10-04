package com.familyfund.application.membership;

import com.familyfund.application.membership.exception.MembershipFeeRuleOverlapException;
import com.familyfund.application.membership.port.MembershipFeeRuleRepository;
import com.familyfund.domain.membership.MembershipFeePeriod;
import com.familyfund.domain.membership.MembershipFeeRule;
import com.familyfund.domain.shared.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MembershipFeeRuleServiceTest {

    private MembershipFeeRuleRepository repository;
    private MembershipFeeRuleService service;

    private final LocalDate start = LocalDate.of(2026, 3, 21);

    private final LocalDate end = LocalDate.of(2026, 9, 22);

    @BeforeEach
    void setUp() {

        repository = mock(MembershipFeeRuleRepository.class);

        service = new MembershipFeeRuleService(repository);
    }

    @Test
    void shouldCreateRuleWithOverlapChecking() {

        MembershipFeeRule rule = createRuleWithOverlapChecking();

        when(repository.existsOverlappingRule(start, end)).thenReturn(false);

        when(repository.save(rule)).thenReturn(rule);

        MembershipFeeRule result = service.createRule(rule);

        assertSame(rule, result);

        verify(repository).existsOverlappingRule(start, end);

        verify(repository).save(rule);
    }

    @Test
    void shouldRejectOverlappingRule() {

        MembershipFeeRule rule = createRuleWithOverlapChecking();

        when(repository.existsOverlappingRule(start, end)).thenReturn(true);

        assertThrows(MembershipFeeRuleOverlapException.class, () -> service.createRule(rule));

        verify(repository).existsOverlappingRule(start, end);

        verify(repository, never()).save(any());
    }

    @Test
    void shouldFindEffectiveRule() {

        MembershipFeeRule rule = createRuleWithOverlapChecking();

        LocalDate date = LocalDate.of(2026, 6, 1);

        when(repository.findEffectiveRule(date)).thenReturn(Optional.of(rule));

        Optional<MembershipFeeRule> result = service.findEffectiveRule(date);

        assertTrue(result.isPresent());
        assertSame(rule, result.get());

        verify(repository).findEffectiveRule(date);
    }

    @Test
    void shouldReturnEmptyWhenNoEffectiveRuleExists() {

        LocalDate date = LocalDate.of(2026, 6, 1);

        when(repository.findEffectiveRule(date)).thenReturn(Optional.empty());

        Optional<MembershipFeeRule> result = service.findEffectiveRule(date);

        assertTrue(result.isEmpty());

        verify(repository).findEffectiveRule(date);
    }

    private MembershipFeeRule createRuleWithOverlapChecking() {

        return new MembershipFeeRule(start, end, Money.of(1_000_000L), MembershipFeePeriod.MONTHLY);
    }
}