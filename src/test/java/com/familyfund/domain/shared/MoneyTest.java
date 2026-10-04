package com.familyfund.domain.shared;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @Test
    void shouldCreateMoneyFromBigDecimal() {
        Money money = Money.of(new BigDecimal("1000.250"));

        assertEquals(new BigDecimal("1000.250"), money.getAmount());
    }

    @Test
    void shouldCreateMoneyFromLong() {
        Money money = Money.of(1_000L);

        assertEquals(new BigDecimal("1000"), money.getAmount());
    }

    @Test
    void shouldAllowZeroAmount() {
        Money money = Money.zero();

        assertTrue(money.isZero());
        assertFalse(money.isPositive());
        assertFalse(money.isNegative());
    }

    @Test
    void shouldRejectNegativeAmount() {
        assertThrows(InvalidMoneyAmountException.class, () -> Money.of(new BigDecimal("-500.125")));
    }

    @Test
    void shouldRejectNullAmount() {
        assertThrows(InvalidMoneyAmountException.class, () -> Money.of(null));
    }

    @Test
    void shouldAllowUpToThreeFractionalDigits() {
        Money money = Money.of(new BigDecimal("1000.123"));

        assertEquals(new BigDecimal("1000.123"), money.getAmount());
    }

    @Test
    void shouldRejectMoreThanThreeFractionalDigits() {
        assertThrows(InvalidMoneyAmountException.class, () -> Money.of(new BigDecimal("1000.1234")));
    }

    @Test
    void shouldAllowTrailingZerosBeyondThreeDecimalPlaces() {
        Money money = Money.of(new BigDecimal("1000.1230"));

        assertEquals(new BigDecimal("1000.1230"), money.getAmount());
    }

    @Test
    void shouldAddMoney() {
        Money first = Money.of(new BigDecimal("1000.250"));
        Money second = Money.of(new BigDecimal("200.125"));

        Money result = first.add(second);

        assertEquals(new BigDecimal("1200.375"), result.getAmount());
    }

    @Test
    void shouldSubtractMoneyWhenResultIsNotNegative() {
        Money first = Money.of(new BigDecimal("1000.250"));
        Money second = Money.of(new BigDecimal("200.125"));

        Money result = first.subtract(second);

        assertEquals(new BigDecimal("800.125"), result.getAmount());
    }

    @Test
    void shouldRejectSubtractionResultingInNegativeMoney() {
        Money first = Money.of(new BigDecimal("100"));
        Money second = Money.of(new BigDecimal("200"));

        assertThrows(InvalidMoneyAmountException.class, () -> first.subtract(second));
    }

    @Test
    void shouldIdentifyPositiveMoney() {
        Money money = Money.of(new BigDecimal("0.001"));

        assertTrue(money.isPositive());
        assertFalse(money.isNegative());
        assertFalse(money.isZero());
    }

    @Test
    void shouldIdentifyZeroMoney() {
        Money money = Money.zero();

        assertTrue(money.isZero());
        assertFalse(money.isPositive());
        assertFalse(money.isNegative());
    }

    @Test
    void shouldCompareMoney() {
        Money first = Money.of(new BigDecimal("1000.125"));
        Money equal = Money.of(new BigDecimal("1000.125"));
        Money greater = Money.of(new BigDecimal("1000.126"));

        assertEquals(0, first.compareTo(equal));
        assertTrue(greater.compareTo(first) > 0);
        assertTrue(first.compareTo(greater) < 0);
    }

    @Test
    void shouldConsiderEqualValuesEqualRegardlessOfScale() {
        Money first = Money.of(new BigDecimal("1000"));
        Money second = Money.of(new BigDecimal("1000.000"));

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void shouldNotChangeOriginalMoneyWhenAdding() {
        Money original = Money.of(new BigDecimal("1000.125"));

        Money result = original.add(Money.of(100));

        assertEquals(new BigDecimal("1000.125"), original.getAmount());

        assertEquals(new BigDecimal("1100.125"), result.getAmount());

        assertNotSame(original, result);
    }

    @Test
    void shouldNotChangeOriginalMoneyWhenSubtracting() {
        Money original = Money.of(new BigDecimal("1000.125"));

        Money result = original.subtract(Money.of(100));

        assertEquals(new BigDecimal("1000.125"), original.getAmount());

        assertEquals(new BigDecimal("900.125"), result.getAmount());

        assertNotSame(original, result);
    }

    @Test
    void shouldReturnReadableString() {
        Money money = Money.of(new BigDecimal("1000.125"));

        assertEquals("1000.125", money.toString());
    }
}