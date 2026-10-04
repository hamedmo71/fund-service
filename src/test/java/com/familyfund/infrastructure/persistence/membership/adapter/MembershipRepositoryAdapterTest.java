package com.familyfund.infrastructure.persistence.membership.adapter;

import com.familyfund.domain.member.Member;
import com.familyfund.domain.membership.Membership;
import com.familyfund.infrastructure.persistence.membership.MembershipJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MembershipRepositoryAdapterTest {

    private MembershipJpaRepository jpaRepository;
    private MembershipRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        jpaRepository = mock(MembershipJpaRepository.class);
        adapter = new MembershipRepositoryAdapter(jpaRepository);
    }

    @Test
    void shouldSaveMembership() {
        Membership membership = createMembership();

        when(jpaRepository.save(membership)).thenReturn(membership);

        Membership result = adapter.save(membership);

        assertSame(membership, result);

        verify(jpaRepository).save(membership);
    }

    @Test
    void shouldFindActiveMembershipByMemberId() {
        Membership membership = createMembership();

        Long memberId = 10L;

        when(jpaRepository.findActiveMembershipByMemberId(memberId)).thenReturn(Optional.of(membership));

        Optional<Membership> result = adapter.findActiveMembershipByMemberId(memberId);

        assertTrue(result.isPresent());
        assertSame(membership, result.get());

        verify(jpaRepository).findActiveMembershipByMemberId(memberId);
    }

    @Test
    void shouldReturnEmptyWhenNoActiveMembershipExists() {
        Long memberId = 10L;

        when(jpaRepository.findActiveMembershipByMemberId(memberId)).thenReturn(Optional.empty());

        Optional<Membership> result = adapter.findActiveMembershipByMemberId(memberId);

        assertTrue(result.isEmpty());

        verify(jpaRepository).findActiveMembershipByMemberId(memberId);
    }

    @Test
    void shouldCheckOverlappingMembership() {
        Long memberId = 10L;

        LocalDate startDate = LocalDate.of(2026, 3, 21);

        LocalDate endDate = LocalDate.of(2026, 9, 22);

        when(jpaRepository.existsOverlappingMembership(memberId, startDate, endDate)).thenReturn(true);

        boolean result = adapter.existsOverlappingMembership(memberId, startDate, endDate);

        assertTrue(result);

        verify(jpaRepository).existsOverlappingMembership(memberId, startDate, endDate);
    }

    @Test
    void shouldReturnFalseWhenNoOverlappingMembershipExists() {
        Long memberId = 10L;

        LocalDate startDate = LocalDate.of(2026, 9, 23);

        LocalDate endDate = LocalDate.of(2026, 12, 31);

        when(jpaRepository.existsOverlappingMembership(memberId, startDate, endDate)).thenReturn(false);

        boolean result = adapter.existsOverlappingMembership(memberId, startDate, endDate);

        assertFalse(result);

        verify(jpaRepository).existsOverlappingMembership(memberId, startDate, endDate);
    }

    private Membership createMembership() {
        Member member = new Member("M001", "Ali", "Ahmadi");

        return new Membership(member, LocalDate.of(2026, 3, 21));
    }
}