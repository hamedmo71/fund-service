package com.familyfund.infrastructure.persistence.membership;

import com.familyfund.domain.member.Member;
import com.familyfund.domain.membership.Membership;
import com.familyfund.domain.membership.MembershipStatus;
import com.familyfund.infrastructure.configuration.FlywayConfig;
import com.familyfund.infrastructure.configuration.PersistenceConfig;
import com.familyfund.infrastructure.configuration.SpringConfig;
import com.familyfund.infrastructure.persistence.member.MemberJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig({SpringConfig.class, PersistenceConfig.class, FlywayConfig.class})
@Transactional
class MembershipJpaRepositoryIntegrationTest {

    @Autowired
    private MembershipJpaRepository membershipRepository;

    @Autowired
    private MemberJpaRepository memberRepository;

    @BeforeEach
    void cleanDatabase() {
        membershipRepository.deleteAll();
        membershipRepository.flush();
    }

    @Test
    void shouldSaveMembership() {
        Member member = createMember();

        Membership membership = new Membership(member, LocalDate.of(2026, 3, 21));

        Membership saved = membershipRepository.save(membership);

        assertNotNull(saved.getId());
        assertEquals(member.getId(), saved.getMember().getId());
        assertEquals(MembershipStatus.ACTIVE, saved.getStatus());
    }

    @Test
    void shouldFindActiveMembershipByMemberId() {
        Member member = createMember();

        Membership membership = new Membership(member, LocalDate.of(2026, 3, 21));

        membershipRepository.saveAndFlush(membership);

        Optional<Membership> result = membershipRepository.findActiveMembershipByMemberId(member.getId());

        assertTrue(result.isPresent());
        assertEquals(membership.getId(), result.get().getId());
    }

    @Test
    void shouldReturnEmptyWhenMemberHasNoActiveMembership() {
        Member member = createMember();

        Membership membership = new Membership(member, LocalDate.of(2026, 3, 21));

        membership.end(LocalDate.of(2026, 9, 22));

        membershipRepository.saveAndFlush(membership);

        Optional<Membership> result = membershipRepository.findActiveMembershipByMemberId(member.getId());

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldDetectOverlappingMembership() {
        Member member = createMember();

        membershipRepository.saveAndFlush(createMembership(member, LocalDate.of(2026, 3, 21), LocalDate.of(2026, 9, 22)));

        boolean result = membershipRepository.existsOverlappingMembership(member.getId(), LocalDate.of(2026, 6, 1), LocalDate.of(2026, 12, 31));

        assertTrue(result);
    }

    @Test
    void shouldNotDetectNonOverlappingMembership() {
        Member member = createMember();

        membershipRepository.saveAndFlush(createMembership(member, LocalDate.of(2026, 3, 21), LocalDate.of(2026, 9, 22)));

        boolean result = membershipRepository.existsOverlappingMembership(member.getId(), LocalDate.of(2026, 9, 23), LocalDate.of(2026, 12, 31));

        assertFalse(result);
    }

    @Test
    void shouldDetectOverlapWhenNewMembershipStartsOnExistingEndDate() {
        Member member = createMember();

        membershipRepository.saveAndFlush(createMembership(member, LocalDate.of(2026, 3, 21), LocalDate.of(2026, 9, 22)));

        boolean result = membershipRepository.existsOverlappingMembership(member.getId(), LocalDate.of(2026, 9, 22), LocalDate.of(2026, 12, 31));

        assertTrue(result);
    }

    @Test
    void shouldDetectOverlapWhenNewMembershipStartsBeforeExistingEndDate() {
        Member member = createMember();

        membershipRepository.saveAndFlush(createMembership(member, LocalDate.of(2026, 3, 21), LocalDate.of(2026, 9, 22)));

        boolean result = membershipRepository.existsOverlappingMembership(member.getId(), LocalDate.of(2026, 9, 1), LocalDate.of(2026, 12, 31));

        assertTrue(result);
    }

    @Test
    void shouldDetectOverlapWithExistingOpenEndedMembership() {
        Member member = createMember();

        membershipRepository.saveAndFlush(createMembership(member, LocalDate.of(2026, 3, 21), null));

        boolean result = membershipRepository.existsOverlappingMembership(member.getId(), LocalDate.of(2026, 9, 23), LocalDate.of(2026, 12, 31));

        assertTrue(result);
    }

    @Test
    void shouldDetectOverlapWhenNewMembershipIsOpenEnded() {
        Member member = createMember();

        membershipRepository.saveAndFlush(createMembership(member, LocalDate.of(2026, 3, 21), LocalDate.of(2026, 9, 22)));

        boolean result = membershipRepository.existsOverlappingMembership(member.getId(), LocalDate.of(2026, 9, 1), null);

        assertTrue(result);
    }

    @Test
    void shouldNotDetectOverlapForDifferentMember() {
        Member firstMember = createMember();
        Member secondMember = createMember();

        membershipRepository.saveAndFlush(createMembership(firstMember, LocalDate.of(2026, 3, 21), LocalDate.of(2026, 9, 22)));

        boolean result = membershipRepository.existsOverlappingMembership(secondMember.getId(), LocalDate.of(2026, 6, 1), LocalDate.of(2026, 12, 31));

        assertFalse(result);
    }

    private Membership createMembership(Member member, LocalDate startDate, LocalDate endDate) {
        Membership membership = new Membership(member, startDate);

        if (endDate != null) {
            membership.end(endDate);
        }

        return membership;
    }

    private Member createMember() {
        Member member = new Member("M-" + System.nanoTime(), "Ali", "Ahmadi");

        return memberRepository.saveAndFlush(member);
    }
}