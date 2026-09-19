package com.familyfund.infrastructure.persistence.member;

import com.familyfund.TestSpringConfig;
import com.familyfund.application.member.port.MemberRepository;
import com.familyfund.domain.member.Member;
import com.familyfund.domain.member.MemberPhone;
import com.familyfund.domain.member.PhoneType;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(TestSpringConfig.class)
@TestPropertySource("classpath:application-test.properties")
@Transactional
class MemberJpaRepositoryIntegrationTest {

    @Autowired
    private MemberRepository repository;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldSaveMember() {
        // Given
        Member member = new Member("TEST-001", "Ali", "Ahmadi");

        // When
        Member saved = repository.save(member);

        // Then
        assertNotNull(saved.getId());
        assertEquals("TEST-001", saved.getMemberCode());
        assertEquals("Ali", saved.getFirstName());
        assertEquals("Ahmadi", saved.getLastName());
    }

    @Test
    void shouldFindMemberByMemberCode() {
        Member member = new Member("TEST-002", "Reza", "Ahmadi");
        repository.save(member);

        Optional<Member> result = repository.findByMemberCode("TEST-002");

        assertTrue(result.isPresent());
        assertEquals("TEST-002", result.get().getMemberCode());
        assertEquals("Reza", result.get().getFirstName());
        assertEquals("Ahmadi", result.get().getLastName());
    }

    @Test
    void shouldReturnEmptyWhenMemberCodeDoesNotExist() {
        Optional<Member> result = repository.findByMemberCode("DOES-NOT-EXIST");

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnTrueWhenMemberCodeExists() {
        Member member = new Member("TEST-003", "Hassan", "Ahmadi");
        repository.save(member);

        boolean exists = repository.existsByMemberCode("TEST-003");

        assertTrue(exists);
    }

    @Test
    void shouldReturnFalseWhenMemberCodeDoesNotExist() {
        boolean exists = repository.existsByMemberCode("DOES-NOT-EXIST");

        assertFalse(exists);
    }

    @Test
    void shouldRejectDuplicateMemberCode() {
        Member firstMember = new Member("TEST-100", "Ali", "Ahmadi");

        Member secondMember = new Member("TEST-100", "Reza", "Mohammadi");

        repository.save(firstMember);

        assertThrows(DataIntegrityViolationException.class, () -> memberJpaRepository.saveAndFlush(secondMember));
    }

    @Test
    void shouldUpdateMemberInformation() {
        Member member = new Member("TEST-200", "Ali", "Ahmadi");

        repository.save(member);

        member.updateInformation("Reza", "Mohammadi", "Updated");

        repository.save(member);

        Optional<Member> result = repository.findByMemberCode("TEST-200");

        assertTrue(result.isPresent());
        assertEquals("Reza", result.get().getFirstName());
        assertEquals("Mohammadi", result.get().getLastName());
        assertEquals("Updated", result.get().getDescription());
    }

    @Test
    void shouldSaveMemberWithPhones() {
        Member member = new Member("TEST-PHONE-001", "Ali", "Ahmadi");

        MemberPhone mobile = new MemberPhone("09121234567", PhoneType.MOBILE, true, "Personal");

        MemberPhone home = new MemberPhone("02112345678", PhoneType.HOME, false, "Home");

        member.addPhone(mobile);
        member.addPhone(home);

        Member saved = repository.save(member);

        memberJpaRepository.flush();

        assertNotNull(saved.getId());
        assertEquals(2, saved.getPhones().size());

        assertTrue(saved.getPhones().stream().anyMatch(p -> p.getPhoneNumber().equals("09121234567")));

        assertTrue(saved.getPhones().stream().anyMatch(p -> p.getPhoneNumber().equals("02112345678")));
    }

    @Test
    void shouldRemovePhoneFromDatabaseWhenRemovedFromMember() {
        Member member = new Member("TEST-PHONE-002", "Reza", "Ahmadi");

        MemberPhone phone = new MemberPhone("09121111111", PhoneType.MOBILE, true, "Personal");

        member.addPhone(phone);

        repository.save(member);
        memberJpaRepository.flush();

        Long phoneId = phone.getId();

        member.removePhone(phone);

        memberJpaRepository.flush();

        entityManager.clear();

        MemberPhone deletedPhone = entityManager.find(MemberPhone.class, phoneId);

        assertNull(deletedPhone);
    }

    @Test
    void shouldDeletePhonesWhenMemberIsDeleted() {
        Member member = new Member("TEST-PHONE-003", "Hassan", "Ahmadi");

        MemberPhone phone = new MemberPhone("09123333333", PhoneType.MOBILE, true, null);

        member.addPhone(phone);

        repository.save(member);
        memberJpaRepository.flush();

        Long memberId = member.getId();
        Long phoneId = phone.getId();

        memberJpaRepository.delete(member);
        memberJpaRepository.flush();

        entityManager.clear();

        assertNull(entityManager.find(Member.class, memberId));
        assertNull(entityManager.find(MemberPhone.class, phoneId));
    }
}