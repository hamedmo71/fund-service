package com.familyfund.domain.member;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberPhoneTest {

    @Test
    void shouldCreateMemberPhone() {
        MemberPhone phone = new MemberPhone("09121234567", PhoneType.MOBILE, true, "Personal phone");

        assertEquals("09121234567", phone.getPhoneNumber());
        assertEquals(PhoneType.MOBILE, phone.getPhoneType());
        assertTrue(phone.isPrimary());
        assertEquals("Personal phone", phone.getDescription());
    }

    @Test
    void shouldCreateNonPrimaryPhone() {
        MemberPhone phone = new MemberPhone("02112345678", PhoneType.HOME, false, null);

        assertEquals("02112345678", phone.getPhoneNumber());
        assertEquals(PhoneType.HOME, phone.getPhoneType());
        assertFalse(phone.isPrimary());
        assertNull(phone.getDescription());
    }
}