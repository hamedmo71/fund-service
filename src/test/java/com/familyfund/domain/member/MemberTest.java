package com.familyfund.domain.member;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    @Test
    void shouldAddPhoneToMember() {
        Member member = new Member(
                "1405-001",
                "Ali",
                "Ahmadi"
        );

        MemberPhone phone = new MemberPhone(
                "09121234567",
                PhoneType.MOBILE,
                true,
                "Personal"
        );

        member.addPhone(phone);

        assertEquals(1, member.getPhones().size());
        assertSame(phone, member.getPhones().getFirst());
        assertSame(member, phone.getMember());
    }

    @Test
    void shouldRemovePhoneFromMember() {
        // Given
        Member member = new Member("1405-001", "Ali", "Ahmadi");

        MemberPhone phone = new MemberPhone("09121234567", PhoneType.MOBILE, true);

        member.addPhone(phone);

        // When
        member.removePhone(phone);

        // Then
        assertTrue(member.getPhones().isEmpty());
        assertNull(phone.getMember());
    }

    @Test
    void shouldUpdateMemberInformation() {
        Member member =
                new Member("1405-001", "Ali", "Ahmadi");

        member.updateInformation(
                "Reza",
                "Mohammadi",
                "Updated description"
        );

        assertEquals("Reza", member.getFirstName());
        assertEquals("Mohammadi", member.getLastName());
        assertEquals("Updated description", member.getDescription());
    }

}