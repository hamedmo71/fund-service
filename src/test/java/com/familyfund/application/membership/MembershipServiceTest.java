package com.familyfund.application.membership;

import com.familyfund.application.member.exception.MemberNotFoundException;
import com.familyfund.application.member.port.MemberRepository;
import com.familyfund.application.membership.exception.MembershipNotFoundException;
import com.familyfund.application.membership.exception.MembershipOverlapException;
import com.familyfund.application.membership.port.MembershipRepository;
import com.familyfund.domain.member.Member;
import com.familyfund.domain.membership.Membership;
import com.familyfund.domain.membership.MembershipStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MembershipServiceTest {

    private MemberRepository memberRepository;
    private MembershipRepository membershipRepository;
    private MembershipService service;

    private Member member;

    private static final LocalDate START = LocalDate.of(2026, 3, 21);

    private static final LocalDate END = LocalDate.of(2026, 9, 22);

    @BeforeEach
    void setUp() {
        memberRepository = mock(MemberRepository.class);
        membershipRepository = mock(MembershipRepository.class);

        service = new MembershipService(membershipRepository, memberRepository);

        member = new Member("M001", "Ali", "Ahmadi");
    }

    @Test
    void shouldCreateMembership() {
        when(memberRepository.findByMemberCode("M001")).thenReturn(Optional.of(member));

        when(membershipRepository.existsOverlappingMembership(member.getId(), START, null)).thenReturn(false);

        Membership savedMembership = new Membership(member, START);

        when(membershipRepository.save(any(Membership.class))).thenReturn(savedMembership);

        Membership result = service.createMembership("M001", START);

        assertSame(savedMembership, result);

        assertEquals(START, result.getStartDate());

        assertNull(result.getEndDate());

        verify(memberRepository).findByMemberCode("M001");

        verify(membershipRepository).existsOverlappingMembership(member.getId(), START, null);

        verify(membershipRepository).save(any(Membership.class));
    }

    @Test
    void shouldRejectMembershipWhenMemberDoesNotExist() {
        when(memberRepository.findByMemberCode("M001")).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> service.createMembership("M001", START));

        verify(membershipRepository, never()).existsOverlappingMembership(anyLong(), any(), any());

        verify(membershipRepository, never()).save(any());
    }

    @Test
    void shouldRejectOverlappingMembership() {
        when(memberRepository.findByMemberCode("M001")).thenReturn(Optional.of(member));

        when(membershipRepository.existsOverlappingMembership(member.getId(), START, null)).thenReturn(true);

        assertThrows(MembershipOverlapException.class, () -> service.createMembership("M001", START));

        verify(membershipRepository).existsOverlappingMembership(member.getId(), START, null);

        verify(membershipRepository, never()).save(any());
    }

    @Test
    void shouldEndMembership() {
        Membership membership = new Membership(member, START);

        when(memberRepository.findByMemberCode("M001")).thenReturn(Optional.of(member));

        when(membershipRepository.findActiveMembershipByMemberId(member.getId())).thenReturn(Optional.of(membership));

        when(membershipRepository.save(membership)).thenReturn(membership);

        Membership result = service.endMembership("M001", END);

        assertSame(membership, result);

        assertEquals(END, result.getEndDate());

        assertEquals(com.familyfund.domain.membership.MembershipStatus.ENDED, result.getStatus());

        verify(memberRepository).findByMemberCode("M001");

        verify(membershipRepository).findActiveMembershipByMemberId(member.getId());

        verify(membershipRepository).save(membership);
    }

    @Test
    void shouldRejectEndingMembershipWhenMemberDoesNotExist() {
        when(memberRepository.findByMemberCode("M001")).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> service.endMembership("M001", END));

        verify(membershipRepository, never()).findActiveMembershipByMemberId(anyLong());

        verify(membershipRepository, never()).save(any());
    }

    @Test
    void shouldRejectEndingMembershipWhenNoActiveMembershipExists() {
        when(memberRepository.findByMemberCode("M001")).thenReturn(Optional.of(member));

        when(membershipRepository.findActiveMembershipByMemberId(member.getId())).thenReturn(Optional.empty());

        assertThrows(MembershipNotFoundException.class, () -> service.endMembership("M001", END));

        verify(membershipRepository).findActiveMembershipByMemberId(member.getId());

        verify(membershipRepository, never()).save(any());
    }

    @Test
    void shouldRejectInvalidEndDate() {
        Membership membership = new Membership(member, START);

        when(memberRepository.findByMemberCode("M001")).thenReturn(Optional.of(member));

        when(membershipRepository.findActiveMembershipByMemberId(member.getId())).thenReturn(Optional.of(membership));

        LocalDate invalidEndDate = LocalDate.of(2026, 3, 20);

        assertThrows(IllegalArgumentException.class, () -> service.endMembership("M001", invalidEndDate));

        verify(membershipRepository, never()).save(any());
    }

    @Test
    void shouldRejectNullStartDate() {

        assertThrows(IllegalArgumentException.class, () -> service.createMembership("M001", null));

        verifyNoInteractions(memberRepository, membershipRepository);
    }

    @Test
    void shouldRejectNullEndDate() {

        assertThrows(IllegalArgumentException.class, () -> service.endMembership("M001", null));

        verifyNoInteractions(memberRepository, membershipRepository);
    }

    @Test
    void shouldRejectEndDateBeforeMembershipStartDate() {

        Membership membership = new Membership(member, START);

        when(memberRepository.findByMemberCode("M001")).thenReturn(Optional.of(member));

        when(membershipRepository.findActiveMembershipByMemberId(member.getId())).thenReturn(Optional.of(membership));

        LocalDate invalidEndDate = START.minusDays(1);

        assertThrows(IllegalArgumentException.class, () -> service.endMembership("M001", invalidEndDate));

        assertNull(membership.getEndDate());

        assertEquals(MembershipStatus.ACTIVE, membership.getStatus());

        verify(membershipRepository, never()).save(any());
    }
}