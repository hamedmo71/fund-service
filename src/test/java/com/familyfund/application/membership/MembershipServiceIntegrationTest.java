package com.familyfund.application.membership;

import com.familyfund.application.member.port.MemberRepository;
import com.familyfund.domain.member.Member;
import com.familyfund.domain.membership.Membership;
import com.familyfund.domain.membership.MembershipStatus;
import com.familyfund.infrastructure.persistence.member.MemberJpaRepository;
import com.familyfund.infrastructure.persistence.membership.MembershipJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(com.familyfund.TestSpringConfig.class)
@TestPropertySource("classpath:application-test.properties")
class MembershipServiceIntegrationTest {

    @Autowired
    private MembershipService membershipService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Autowired
    private MembershipJpaRepository membershipJpaRepository;

    private static final LocalDate START_DATE = LocalDate.of(2026, 3, 21);

    private static final LocalDate END_DATE = LocalDate.of(2026, 9, 22);

    @BeforeEach
    void setUp() {
        membershipJpaRepository.deleteAll();
        membershipJpaRepository.flush();

        memberJpaRepository.deleteAll();
        memberJpaRepository.flush();
    }

    @Test
    void shouldCreateMembershipThroughService() {

        Member member = memberRepository.save(new Member("M-INT-001", "Ali", "Ahmadi"));

        Membership result = membershipService.createMembership("M-INT-001", START_DATE);

        assertNotNull(result.getId());
        assertEquals(START_DATE, result.getStartDate());
        assertNull(result.getEndDate());
        assertEquals(MembershipStatus.ACTIVE, result.getStatus());
        assertEquals(member.getId(), result.getMember().getId());

        assertEquals(1, membershipJpaRepository.count());
    }

    @Test
    void shouldEndMembershipThroughService() {

        Member member = memberRepository.save(new Member("M-INT-002", "Reza", "Ahmadi"));

        membershipService.createMembership("M-INT-002", START_DATE);

        Membership result = membershipService.endMembership("M-INT-002", END_DATE);

        assertNotNull(result.getId());
        assertEquals(START_DATE, result.getStartDate());
        assertEquals(END_DATE, result.getEndDate());
        assertEquals(MembershipStatus.ENDED, result.getStatus());

        assertEquals(1, membershipJpaRepository.count());
    }

    @Test
    void shouldCreateMembershipAndThenEndIt() {

        Member member = memberRepository.save(new Member("M-INT-003", "Hamed", "Mohammadi"));

        Membership created = membershipService.createMembership("M-INT-003", START_DATE);

        assertEquals(MembershipStatus.ACTIVE, created.getStatus());

        Membership ended = membershipService.endMembership("M-INT-003", END_DATE);

        assertEquals(MembershipStatus.ENDED, ended.getStatus());
        assertEquals(END_DATE, ended.getEndDate());

        assertEquals(1, membershipJpaRepository.count());
    }
}