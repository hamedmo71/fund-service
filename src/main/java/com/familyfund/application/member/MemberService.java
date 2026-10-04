package com.familyfund.application.member;

import com.familyfund.application.member.exception.MemberCodeAlreadyExistsException;
import com.familyfund.application.member.exception.MemberNotFoundException;
import com.familyfund.application.member.port.MemberRepository;
import com.familyfund.domain.member.Member;
import com.familyfund.domain.member.MemberPhone;
import com.familyfund.domain.member.PhoneType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MemberService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemberService.class);

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member registerMember(String memberCode, String firstName, String lastName) {

        LOGGER.debug("Registering member with code={}", memberCode);

        if (memberRepository.existsByMemberCode(memberCode)) {
            LOGGER.warn("Member registration rejected because member code already exists: {}", memberCode);
            throw new MemberCodeAlreadyExistsException(memberCode);
        }

        Member member = new Member(memberCode, firstName, lastName);
        Member savedMember = memberRepository.save(member);
        LOGGER.info("Member registered successfully: {}", memberCode);

        return savedMember;
    }

    public Member findMemberByCode(String memberCode) {
        LOGGER.debug("Finding member with code={}", memberCode);
        return memberRepository.findByMemberCode(memberCode)
                .orElseThrow(() -> {
                    LOGGER.warn("Member {} not found", memberCode);
                    return new MemberNotFoundException(memberCode);
                });
    }

    public Member updateMember(String memberCode, String firstName, String lastName, String description) {
        LOGGER.debug("Updating member with code={} , first name = {}, last name = {} and description = {}", memberCode, firstName, lastName, description);
        Member member = memberRepository.findByMemberCode(memberCode).orElseThrow(() -> new MemberNotFoundException(memberCode));

        member.updateInformation(firstName, lastName, description);
        Member updatedMember = memberRepository.save(member);
        LOGGER.info("Member updated successfully: {}", memberCode);

        return updatedMember;
    }

    public Member addPhone(String memberCode, String phoneNumber, PhoneType phoneType, boolean primary, String description) {
        LOGGER.debug("Adding phone for member {}", memberCode);
        Member member = memberRepository.findByMemberCode(memberCode).orElseThrow(() -> new MemberNotFoundException(memberCode));

        MemberPhone phone = new MemberPhone(phoneNumber, phoneType, primary, description);

        member.addPhone(phone);
        Member savedMember = memberRepository.save(member);

        LOGGER.info("Phone added successfully for memberCode={}", memberCode);

        return savedMember;
    }

    public Member removePhone(String memberCode, String phoneNumber) {
        LOGGER.debug("Removing phone for member {}", memberCode);
        Member member = memberRepository.findByMemberCode(memberCode)
                .orElseThrow(() -> {
                    LOGGER.warn("Member {} not found", memberCode);
                    return new MemberNotFoundException(memberCode);
                });

        MemberPhone phone = member.getPhones().stream()
                .filter(p -> p.getPhoneNumber().equals(phoneNumber))
                .findFirst()
                .orElseThrow(() -> {
                    LOGGER.warn("Phone number {} does not exist in {} {}'s phone list.", phoneNumber, member.getFirstName(), member.getLastName());
                    return new IllegalArgumentException("Phone not found: " + phoneNumber);
                });

        member.removePhone(phone);
        Member savedMember = memberRepository.save(member);
        LOGGER.info("Phone removed successfully: {}", memberCode);

        return savedMember;
    }
}