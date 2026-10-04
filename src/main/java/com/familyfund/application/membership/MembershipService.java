package com.familyfund.application.membership;

import com.familyfund.application.member.exception.MemberNotFoundException;
import com.familyfund.application.member.port.MemberRepository;
import com.familyfund.application.membership.exception.MembershipNotFoundException;
import com.familyfund.application.membership.exception.MembershipOverlapException;
import com.familyfund.application.membership.port.MembershipRepository;
import com.familyfund.domain.member.Member;
import com.familyfund.domain.membership.Membership;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class MembershipService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MembershipService.class);

    private final MembershipRepository membershipRepository;
    private final MemberRepository memberRepository;

    public MembershipService(MembershipRepository membershipRepository, MemberRepository memberRepository) {

        this.membershipRepository = membershipRepository;
        this.memberRepository = memberRepository;
    }

    public Membership createMembership(String memberCode, LocalDate startDate) {

        if (startDate == null) {
            throw new IllegalArgumentException("Membership start date must not be null");
        }

        LOGGER.debug("Creating membership: memberCode={}, startDate={}", memberCode, startDate);

        Member member = memberRepository.findByMemberCode(memberCode).orElseThrow(() -> new MemberNotFoundException(memberCode));

        if (membershipRepository.existsOverlappingMembership(member.getId(), startDate, null)) {
            LOGGER.warn("Membership creation rejected due to overlap: memberCode={}, startDate={}", memberCode, startDate);

            throw new MembershipOverlapException();
        }

        Membership membership = new Membership(member, startDate);

        Membership savedMembership = membershipRepository.save(membership);

        LOGGER.info("Membership created successfully: memberCode={}, startDate={}", memberCode, startDate);

        return savedMembership;
    }

    public Membership endMembership(String memberCode, LocalDate endDate) {

        if (endDate == null) {
            throw new IllegalArgumentException("Membership end date must not be null");
        }

        LOGGER.debug("Ending membership: memberCode={}, endDate={}", memberCode, endDate);

        Member member = memberRepository.findByMemberCode(memberCode).orElseThrow(() -> new MemberNotFoundException(memberCode));

        Membership membership = membershipRepository.findActiveMembershipByMemberId(member.getId()).orElseThrow(() -> new MembershipNotFoundException(memberCode));

        membership.end(endDate);

        Membership savedMembership = membershipRepository.save(membership);

        LOGGER.info("Membership ended successfully: memberCode={}, endDate={}", memberCode, endDate);

        return savedMembership;
    }
}