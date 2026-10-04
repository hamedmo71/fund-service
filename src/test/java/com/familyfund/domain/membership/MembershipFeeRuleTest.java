package com.familyfund.domain.membership;

import com.familyfund.domain.shared.Money;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class MembershipFeeRuleTest {

    private static final LocalDate START = LocalDate.of(2026, 3, 21);

    private static final LocalDate END = LocalDate.of(2026, 9, 22);

    @Test
    void shouldCreateMonthlyMembershipFeeRule() {

        Money amount = Money.of(1_000_000L);

        MembershipFeeRule rule = new MembershipFeeRule(START, END, amount, MembershipFeePeriod.MONTHLY);

        assertEquals(START, rule.getEffectiveFrom());
        assertEquals(END, rule.getEffectiveTo());
        assertEquals(amount, rule.getAmount());
        assertEquals(MembershipFeePeriod.MONTHLY, rule.getPeriod());
    }

    @Test
    void shouldBeEffectiveOnStartDate() {

        MembershipFeeRule rule = createRule();

        assertTrue(rule.isEffectiveOn(START));
    }

    @Test
    void shouldNotBeEffectiveBeforeStartDate() {

        MembershipFeeRule rule = createRule();

        assertFalse(rule.isEffectiveOn(LocalDate.of(2026, 3, 20)));
    }

    @Test
    void shouldBeEffectiveOnEndDate() {

        MembershipFeeRule rule = createRule();

        assertTrue(rule.isEffectiveOn(END));
    }

    @Test
    void shouldNotBeEffectiveAfterEndDate() {

        MembershipFeeRule rule = createRule();

        assertFalse(rule.isEffectiveOn(LocalDate.of(2026, 9, 23)));
    }

    @Test
    void shouldAllowOpenEndedRule() {

        MembershipFeeRule rule = new MembershipFeeRule(START, null, Money.of(1_000_000L), MembershipFeePeriod.MONTHLY);

        assertTrue(rule.isEffectiveOn(LocalDate.of(2030, 1, 1)));
    }

    @Test
    void shouldRejectZeroAmount() {

        assertThrows(IllegalArgumentException.class, () -> new MembershipFeeRule(START, END, Money.zero(), MembershipFeePeriod.MONTHLY));
    }

    @Test
    void shouldRejectNullAmount() {

        assertThrows(IllegalArgumentException.class, () -> new MembershipFeeRule(START, END, null, MembershipFeePeriod.MONTHLY));
    }

    @Test
    void shouldRejectInvalidDateRange() {

        assertThrows(IllegalArgumentException.class, () -> new MembershipFeeRule(END, START, Money.of(1_000_000L), MembershipFeePeriod.MONTHLY));
    }

    @Test
    void shouldRejectNullEffectiveFrom() {

        assertThrows(IllegalArgumentException.class, () -> new MembershipFeeRule(null, END, Money.of(1_000_000L), MembershipFeePeriod.MONTHLY));
    }

    @Test
    void shouldRejectNullPeriod() {

        assertThrows(IllegalArgumentException.class, () -> new MembershipFeeRule(START, END, Money.of(1_000_000L), null));
    }

    private MembershipFeeRule createRule() {

        return new MembershipFeeRule(START, END, Money.of(1_000_000L), MembershipFeePeriod.MONTHLY);
    }
}