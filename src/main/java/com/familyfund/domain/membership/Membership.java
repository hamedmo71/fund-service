package com.familyfund.domain.membership;

import com.familyfund.domain.member.Member;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "membership")
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(name = "fk_membership_member"))
    private Member member;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private MembershipStatus status;

    protected Membership() {}

    public Membership(Member member, LocalDate startDate) {
        if (member == null) {
            throw new IllegalArgumentException("Member must not be null");
        }

        if (startDate == null) {
            throw new IllegalArgumentException("Start date must not be null");
        }

        this.member = member;
        this.startDate = startDate;
        this.status = MembershipStatus.ACTIVE;
    }

    public void end(LocalDate endDate) {
        if (endDate == null) {
            throw new IllegalArgumentException("End date must not be null");
        }

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date must not be before start date");
        }

        if (status == MembershipStatus.ENDED) {
            throw new IllegalStateException("Membership has already ended");
        }

        this.endDate = endDate;
        this.status = MembershipStatus.ENDED;
    }

    public boolean isActiveOn(LocalDate date) {
        if (date == null) {
            return false;
        }

        if (date.isBefore(startDate)) {
            return false;
        }

        return endDate == null || !date.isAfter(endDate);
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public MembershipStatus getStatus() {
        return status;
    }
}