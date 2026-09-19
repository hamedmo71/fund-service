package com.familyfund.application.member;

import com.familyfund.application.member.exception.MemberCodeAlreadyExistsException;
import com.familyfund.application.member.exception.MemberNotFoundException;
import com.familyfund.application.member.port.MemberRepository;
import com.familyfund.domain.member.Member;
import com.familyfund.domain.member.MemberPhone;
import com.familyfund.domain.member.PhoneType;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemberServiceTest {

    private final MemberRepository memberRepository = mock(MemberRepository.class);

    private final MemberService memberService = new MemberService(memberRepository);

    @Test
    void shouldRegisterNewMember() {
        // Given
        String memberCode = "1405-001";
        String firstName = "Ali";
        String lastName = "Ahmadi";

        when(memberRepository.existsByMemberCode(memberCode)).thenReturn(false);

        Member savedMember = new Member(memberCode, firstName, lastName);

        when(memberRepository.save(any(Member.class))).thenReturn(savedMember);

        // When
        Member result = memberService.registerMember(memberCode, firstName, lastName);

        // Then
        assertNotNull(result);
        assertEquals(memberCode, result.getMemberCode());
        assertEquals(firstName, result.getFirstName());
        assertEquals(lastName, result.getLastName());

        verify(memberRepository).existsByMemberCode(memberCode);
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    void shouldNotRegisterMemberWhenMemberCodeAlreadyExists() {
        // Given
        String memberCode = "1405-001";

        when(memberRepository.existsByMemberCode(memberCode)).thenReturn(true);

        // When / Then
        assertThrows(MemberCodeAlreadyExistsException.class, () -> memberService.registerMember(memberCode, "Ali", "Ahmadi"));

        verify(memberRepository).existsByMemberCode(memberCode);
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    void shouldFindMemberByCode() {
        String memberCode = "1405-001";

        Member member = new Member(memberCode, "Ali", "Ahmadi");

        when(memberRepository.findByMemberCode(memberCode)).thenReturn(Optional.of(member));

        Member result = memberService.findMemberByCode(memberCode);

        assertNotNull(result);
        assertEquals(memberCode, result.getMemberCode());
        assertEquals("Ali", result.getFirstName());
        assertEquals("Ahmadi", result.getLastName());

        verify(memberRepository).findByMemberCode(memberCode);
    }

    @Test
    void shouldThrowExceptionWhenMemberDoesNotExist() {
        String memberCode = "NOT-EXIST";

        when(memberRepository.findByMemberCode(memberCode)).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> memberService.findMemberByCode(memberCode));

        verify(memberRepository).findByMemberCode(memberCode);
    }

    @Test
    void shouldUpdateMemberInformation() {
        String memberCode = "1405-001";
        Member member = new Member(memberCode, "Ali", "Ahmadi");

        when(memberRepository.findByMemberCode(memberCode)).thenReturn(Optional.of(member));

        when(memberRepository.save(member)).thenReturn(member);

        Member result = memberService.updateMember(memberCode, "Reza", "Mohammadi", "Updated description");

        assertEquals("Reza", result.getFirstName());
        assertEquals("Mohammadi", result.getLastName());
        assertEquals("Updated description", result.getDescription());

        verify(memberRepository).findByMemberCode(memberCode);
        verify(memberRepository).save(member);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingMember() {
        String memberCode = "NOT-EXIST";

        when(memberRepository.findByMemberCode(memberCode)).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> memberService.updateMember(memberCode, "Reza", "Mohammadi", "Updated description"));

        verify(memberRepository).findByMemberCode(memberCode);
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    void shouldAddPhoneToMember() {
        String memberCode = "1405-001";

        Member member = new Member(memberCode, "Ali", "Ahmadi");

        when(memberRepository.findByMemberCode(memberCode)).thenReturn(Optional.of(member));

        when(memberRepository.save(member)).thenReturn(member);

        Member result = memberService.addPhone(memberCode, "09121234567", PhoneType.MOBILE, true, "Personal");

        assertEquals(1, result.getPhones().size());

        MemberPhone phone = result.getPhones().getFirst();

        assertEquals("09121234567", phone.getPhoneNumber());
        assertEquals(PhoneType.MOBILE, phone.getPhoneType());
        assertTrue(phone.isPrimary());
        assertEquals("Personal", phone.getDescription());
        assertSame(member, phone.getMember());

        verify(memberRepository).findByMemberCode(memberCode);
        verify(memberRepository).save(member);
    }

    @Test
    void shouldThrowExceptionWhenAddingPhoneToNonExistingMember() {
        String memberCode = "NOT-EXIST";

        when(memberRepository.findByMemberCode(memberCode)).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> memberService.addPhone(memberCode, "09121234567", PhoneType.MOBILE, true, "Personal"));

        verify(memberRepository).findByMemberCode(memberCode);
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    void shouldRemovePhoneFromMember() {
        String memberCode = "1405-001";

        Member member = new Member(memberCode, "Ali", "Ahmadi");

        MemberPhone phone = new MemberPhone("09121234567", PhoneType.MOBILE, true, "Personal");

        member.addPhone(phone);

        when(memberRepository.findByMemberCode(memberCode)).thenReturn(Optional.of(member));

        when(memberRepository.save(member)).thenReturn(member);

        Member result = memberService.removePhone(memberCode, "09121234567");

        assertTrue(result.getPhones().isEmpty());
        assertNull(phone.getMember());

        verify(memberRepository).findByMemberCode(memberCode);
        verify(memberRepository).save(member);
    }

    @Test
    void shouldThrowExceptionWhenRemovingPhoneFromNonExistingMember() {
        String memberCode = "NOT-EXIST";

        when(memberRepository.findByMemberCode(memberCode)).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> memberService.removePhone(memberCode, "09121234567"));

        verify(memberRepository).findByMemberCode(memberCode);
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    void shouldThrowExceptionWhenRemovingNonExistingPhone() {
        String memberCode = "1405-001";

        Member member = new Member(memberCode, "Ali", "Ahmadi");

        when(memberRepository.findByMemberCode(memberCode)).thenReturn(Optional.of(member));

        assertThrows(IllegalArgumentException.class, () -> memberService.removePhone(memberCode, "09129999999"));

        verify(memberRepository).findByMemberCode(memberCode);
        verify(memberRepository, never()).save(any(Member.class));
    }
}