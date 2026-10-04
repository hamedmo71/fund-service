package com.familyfund.domain.shared;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Money implements Comparable<Money> {

    private static final int MAX_SCALE = 3;

    @Column(name = "amount", nullable = false, precision = 19, scale = 3)
    private BigDecimal amount;

    protected Money() {
    }

    private Money(BigDecimal amount) {
        validate(amount);
        this.amount = amount;
    }

    public static Money of(BigDecimal amount) {
        return new Money(amount);
    }

    public static Money of(long amount) {
        return new Money(BigDecimal.valueOf(amount));
    }

    public static Money zero() {
        return new Money(BigDecimal.ZERO);
    }

    private static void validate(BigDecimal amount) {

        if (amount == null) {
            throw new InvalidMoneyAmountException("Money amount must not be null");
        }

        if (amount.signum() < 0) {
            throw new InvalidMoneyAmountException("Money amount cannot be negative");
        }

        if (amount.stripTrailingZeros().scale() > MAX_SCALE) {
            throw new InvalidMoneyAmountException("Money amount must not have more than " + MAX_SCALE + " fractional digits");
        }
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Money add(Money other) {

        Objects.requireNonNull(other, "Money to add must not be null");

        return Money.of(this.amount.add(other.amount));
    }

    public Money subtract(Money other) {

        Objects.requireNonNull(other, "Money to subtract must not be null");

        BigDecimal result = this.amount.subtract(other.amount);

        return Money.of(result);
    }

    public boolean isZero() {
        return amount.signum() == 0;
    }

    public boolean isPositive() {
        return amount.signum() > 0;
    }

    public boolean isNegative() {
        return amount.signum() < 0;
    }

    @Override
    public int compareTo(Money other) {

        Objects.requireNonNull(other, "Money to compare must not be null");

        return this.amount.compareTo(other.amount);
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof Money other)) {
            return false;
        }

        return this.amount.compareTo(other.amount) == 0;
    }

    @Override
    public int hashCode() {
        return amount.stripTrailingZeros().hashCode();
    }

    @Override
    public String toString() {
        return amount.toPlainString();
    }
}