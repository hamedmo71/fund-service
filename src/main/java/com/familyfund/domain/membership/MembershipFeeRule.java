package com.familyfund.domain.membership;

import com.familyfund.domain.shared.Money;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "membership_fee_rule")
public class MembershipFeeRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "effective_from", nullable = false)
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    private LocalDate effectiveTo;

    @Embedded
    private Money amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "period", nullable = false, length = 20)
    private MembershipFeePeriod period;

    protected MembershipFeeRule() {
    }

    public MembershipFeeRule(LocalDate effectiveFrom, LocalDate effectiveTo, Money amount, MembershipFeePeriod period) {

        if (effectiveFrom == null) {
            throw new IllegalArgumentException("Effective from must not be null");
        }

        if (amount == null) {
            throw new IllegalArgumentException("Membership fee amount must not be null");
        }

        if (!amount.isPositive()) {
            throw new IllegalArgumentException("Membership fee amount must be positive");
        }

        if (period == null) {
            throw new IllegalArgumentException("Membership fee period must not be null");
        }

        if (effectiveTo != null && effectiveTo.isBefore(effectiveFrom)) {

            throw new IllegalArgumentException("Effective to must not be before effective from");
        }

        this.effectiveFrom = effectiveFrom;
        this.effectiveTo = effectiveTo;
        this.amount = amount;
        this.period = period;
    }

    public boolean isEffectiveOn(LocalDate date) {

        if (date == null) {
            return false;
        }

        boolean afterOrEqualStart = !date.isBefore(effectiveFrom);

        boolean beforeOrEqualEnd = effectiveTo == null || !date.isAfter(effectiveTo);

        return afterOrEqualStart && beforeOrEqualEnd;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getEffectiveFrom() {
        return effectiveFrom;
    }

    public LocalDate getEffectiveTo() {
        return effectiveTo;
    }

    public Money getAmount() {
        return amount;
    }

    public MembershipFeePeriod getPeriod() {
        return period;
    }
}