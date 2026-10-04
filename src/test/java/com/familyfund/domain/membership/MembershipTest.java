package com.familyfund.domain.membership;

import com.familyfund.domain.member.Member;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class MembershipTest {

    private static final LocalDate START = LocalDate.of(2026, 3, 21);

    private static final LocalDate END = LocalDate.of(2026, 9, 22);

    @Test
    void shouldCreateActiveMembership() {
        Member member = createMember();

        Membership membership = new Membership(member, START);

        assertNull(membership.getId());
        assertSame(member, membership.getMember());
        assertEquals(START, membership.getStartDate());
        assertNull(membership.getEndDate());
        assertEquals(MembershipStatus.ACTIVE, membership.getStatus());
    }

    @Test
    void shouldRejectNullMember() {
        assertThrows(IllegalArgumentException.class, () -> new Membership(null, START));
    }

    @Test
    void shouldRejectNullStartDate() {
        Member member = createMember();

        assertThrows(IllegalArgumentException.class, () -> new Membership(member, null));
    }

    @Test
    void shouldEndMembership() {
        Member member = createMember();

        Membership membership = new Membership(member, START);

        membership.end(END);

        assertEquals(END, membership.getEndDate());
        assertEquals(MembershipStatus.ENDED, membership.getStatus());
    }

    @Test
    void shouldRejectEndDateBeforeStartDate() {
        Member member = createMember();

        Membership membership = new Membership(member, START);

        LocalDate invalidEndDate = LocalDate.of(2026, 3, 20);

        assertThrows(IllegalArgumentException.class, () -> membership.end(invalidEndDate));
    }

    @Test
    void shouldRejectNullEndDate() {
        Member member = createMember();

        Membership membership = new Membership(member, START);

        assertThrows(IllegalArgumentException.class, () -> membership.end(null));
    }

    @Test
    void shouldRejectEndingAlreadyEndedMembership() {
        Member member = createMember();

        Membership membership = new Membership(member, START);

        membership.end(END);

        assertThrows(IllegalStateException.class, () -> membership.end(LocalDate.of(2026, 10, 1)));
    }

    @Test
    void shouldBeActiveOnStartDate() {
        Membership membership = createEndedMembership();

        assertTrue(membership.isActiveOn(START));
    }

    @Test
    void shouldBeActiveOnEndDate() {
        Membership membership = createEndedMembership();

        assertTrue(membership.isActiveOn(END));
    }

    @Test
    void shouldNotBeActiveBeforeStartDate() {
        Membership membership = createEndedMembership();

        assertFalse(membership.isActiveOn(LocalDate.of(2026, 3, 20)));
    }

    @Test
    void shouldNotBeActiveAfterEndDate() {
        Membership membership = createEndedMembership();

        assertFalse(membership.isActiveOn(LocalDate.of(2026, 9, 23)));
    }

    @Test
    void shouldBeActiveIndefinitelyWhenNoEndDateExists() {
        Member member = createMember();

        Membership membership = new Membership(member, START);

        assertTrue(membership.isActiveOn(LocalDate.of(2030, 1, 1)));
    }

    @Test
    void shouldReturnFalseForNullDate() {
        Membership membership = createMembership();

        assertFalse(membership.isActiveOn(null));
    }

    private Membership createMembership() {
        return new Membership(createMember(), START);
    }

    private Membership createEndedMembership() {
        Membership membership = new Membership(createMember(), START);

        membership.end(END);

        return membership;
    }

    private Member createMember() {
        return new Member("M001", "Ali", "Ahmadi");
    }
}