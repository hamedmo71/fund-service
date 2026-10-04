package com.familyfund.infrastructure.persistence.membership;

import com.familyfund.domain.membership.MembershipFeePeriod;
import com.familyfund.domain.membership.MembershipFeeRule;
import com.familyfund.domain.shared.Money;
import com.familyfund.infrastructure.configuration.FlywayConfig;
import com.familyfund.infrastructure.configuration.PersistenceConfig;
import com.familyfund.infrastructure.configuration.SpringConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig({SpringConfig.class, PersistenceConfig.class, FlywayConfig.class})
@Transactional
class MembershipFeeRuleJpaRepositoryIntegrationTest {

    @Autowired
    private MembershipFeeRuleJpaRepository repository;

    private static final LocalDate START_DATE = LocalDate.of(2026, 3, 21);

    private static final LocalDate END_DATE = LocalDate.of(2026, 9, 22);

    @BeforeEach
    void cleanDatabase() {
        repository.deleteAll();
        repository.flush();
    }

    @Test
    void shouldSaveAndLoadMembershipFeeRule() {

        Money amount = Money.of(new BigDecimal("1000000.125"));

        MembershipFeeRule rule = new MembershipFeeRule(START_DATE, END_DATE, amount, MembershipFeePeriod.MONTHLY);

        MembershipFeeRule saved = repository.saveAndFlush(rule);

        assertNotNull(saved.getId());

        MembershipFeeRule loaded = repository.findById(saved.getId()).orElseThrow();

        assertEquals(START_DATE, loaded.getEffectiveFrom());
        assertEquals(END_DATE, loaded.getEffectiveTo());
        assertEquals(MembershipFeePeriod.MONTHLY, loaded.getPeriod());

        assertEquals(amount, loaded.getAmount());

        assertEquals(new BigDecimal("1000000.125"), loaded.getAmount().getAmount());
    }

    @Test
    void shouldSaveAndLoadWholeNumberMoneyAmount() {

        Money amount = Money.of(1_000_000L);

        MembershipFeeRule rule = new MembershipFeeRule(START_DATE, END_DATE, amount, MembershipFeePeriod.MONTHLY);

        MembershipFeeRule saved = repository.saveAndFlush(rule);

        MembershipFeeRule loaded = repository.findById(saved.getId()).orElseThrow();

        assertEquals(amount, loaded.getAmount());
    }

    @Test
    void shouldSaveAndLoadMoneyWithThreeDecimalDigits() {

        Money amount = Money.of(new BigDecimal("1234567.891"));

        MembershipFeeRule rule = new MembershipFeeRule(START_DATE, END_DATE, amount, MembershipFeePeriod.MONTHLY);

        MembershipFeeRule saved = repository.saveAndFlush(rule);

        MembershipFeeRule loaded = repository.findById(saved.getId()).orElseThrow();

        assertEquals(new BigDecimal("1234567.891"), loaded.getAmount().getAmount());
    }

    @Test
    void shouldFindEffectiveRule() {

        MembershipFeeRule rule = new MembershipFeeRule(START_DATE, END_DATE, Money.of(1_000_000L), MembershipFeePeriod.MONTHLY);

        repository.saveAndFlush(rule);

        Optional<MembershipFeeRule> result = repository.findEffectiveRule(LocalDate.of(2026, 6, 1));

        assertTrue(result.isPresent());

        MembershipFeeRule found = result.get();

        assertEquals(rule.getId(), found.getId());
        assertEquals(START_DATE, found.getEffectiveFrom());
        assertEquals(END_DATE, found.getEffectiveTo());
        assertEquals(Money.of(1_000_000L), found.getAmount());
        assertEquals(MembershipFeePeriod.MONTHLY, found.getPeriod());
    }

    @Test
    void shouldReturnEmptyWhenNoEffectiveRuleExists() {

        MembershipFeeRule rule = new MembershipFeeRule(START_DATE, END_DATE, Money.of(1_000_000L), MembershipFeePeriod.MONTHLY);

        repository.saveAndFlush(rule);

        Optional<MembershipFeeRule> result = repository.findEffectiveRule(LocalDate.of(2026, 9, 23));

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldFindRuleOnEffectiveFromDate() {

        MembershipFeeRule rule = new MembershipFeeRule(START_DATE, END_DATE, Money.of(1_000_000L), MembershipFeePeriod.MONTHLY);

        repository.saveAndFlush(rule);

        Optional<MembershipFeeRule> result = repository.findEffectiveRule(START_DATE);

        assertTrue(result.isPresent());
        assertEquals(rule.getId(), result.get().getId());
    }

    @Test
    void shouldFindRuleOnEffectiveToDate() {

        MembershipFeeRule rule = new MembershipFeeRule(START_DATE, END_DATE, Money.of(1_000_000L), MembershipFeePeriod.MONTHLY);

        repository.saveAndFlush(rule);

        Optional<MembershipFeeRule> result = repository.findEffectiveRule(END_DATE);

        assertTrue(result.isPresent());
        assertEquals(rule.getId(), result.get().getId());
    }

    @Test
    void shouldNotFindRuleBeforeEffectiveFromDate() {

        MembershipFeeRule rule = new MembershipFeeRule(START_DATE, END_DATE, Money.of(1_000_000L), MembershipFeePeriod.MONTHLY);

        repository.saveAndFlush(rule);

        Optional<MembershipFeeRule> result = repository.findEffectiveRule(START_DATE.minusDays(1));

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldNotFindRuleAfterEffectiveToDate() {

        MembershipFeeRule rule = new MembershipFeeRule(START_DATE, END_DATE, Money.of(1_000_000L), MembershipFeePeriod.MONTHLY);

        repository.saveAndFlush(rule);

        Optional<MembershipFeeRule> result = repository.findEffectiveRule(END_DATE.plusDays(1));

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldDetectOverlappingRule() {

        MembershipFeeRule existingRule = new MembershipFeeRule(START_DATE, END_DATE, Money.of(1_000_000L), MembershipFeePeriod.MONTHLY);

        repository.saveAndFlush(existingRule);

        boolean result = repository.existsOverlappingRule(LocalDate.of(2026, 6, 1), LocalDate.of(2026, 12, 31));

        assertTrue(result);
    }

    @Test
    void shouldNotDetectNonOverlappingRule() {

        MembershipFeeRule existingRule = new MembershipFeeRule(START_DATE, END_DATE, Money.of(1_000_000L), MembershipFeePeriod.MONTHLY);

        repository.saveAndFlush(existingRule);

        boolean result = repository.existsOverlappingRule(END_DATE.plusDays(1), LocalDate.of(2026, 12, 31));

        assertFalse(result);
    }

    @Test
    void shouldDetectOverlapWhenNewRuleHasNoEndDate() {

        MembershipFeeRule existingRule = new MembershipFeeRule(START_DATE, END_DATE, Money.of(1_000_000L), MembershipFeePeriod.MONTHLY);

        repository.saveAndFlush(existingRule);

        boolean result = repository.existsOverlappingRule(LocalDate.of(2026, 9, 1), null);

        assertTrue(result);
    }

    @Test
    void shouldDetectOverlapWithExistingOpenEndedRule() {

        MembershipFeeRule existingRule = new MembershipFeeRule(START_DATE, null, Money.of(1_000_000L), MembershipFeePeriod.MONTHLY);

        repository.saveAndFlush(existingRule);

        boolean result = repository.existsOverlappingRule(LocalDate.of(2030, 1, 1), LocalDate.of(2030, 12, 31));

        assertTrue(result);
    }

    @Test
    void shouldNotDetectOverlapWhenNewRuleStartsAfterExistingRuleEnds() {

        MembershipFeeRule existingRule = new MembershipFeeRule(START_DATE, END_DATE, Money.of(1_000_000L), MembershipFeePeriod.MONTHLY);

        repository.saveAndFlush(existingRule);

        boolean result = repository.existsOverlappingRule(END_DATE.plusDays(1), LocalDate.of(2026, 12, 31));

        assertFalse(result);
    }
}