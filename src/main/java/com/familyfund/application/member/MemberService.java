package com.familyfund.application.member;

import com.familyfund.application.member.exception.MemberCodeAlreadyExistsException;
import com.familyfund.application.member.exception.MemberNotFoundException;
import com.familyfund.application.member.port.MemberRepository;
import com.familyfund.domain.member.Member;
import com.familyfund.domain.member.MemberPhone;
import com.familyfund.domain.member.PhoneType;

public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member registerMember(String memberCode, String firstName, String lastName) {
        if (memberRepository.existsByMemberCode(memberCode)) {
            throw new MemberCodeAlreadyExistsException(memberCode);
        }

        Member member = new Member(memberCode, firstName, lastName);

        return memberRepository.save(member);
    }

    public Member findMemberByCode(String memberCode) {
        return memberRepository.findByMemberCode(memberCode).orElseThrow(() -> new MemberNotFoundException(memberCode));
    }

    public Member updateMember(String memberCode, String firstName, String lastName, String description) {
        Member member = memberRepository.findByMemberCode(memberCode).orElseThrow(() -> new MemberNotFoundException(memberCode));

        member.updateInformation(firstName, lastName, description);

        return memberRepository.save(member);
    }

    public Member addPhone(String memberCode, String phoneNumber, PhoneType phoneType, boolean primary, String description) {
        Member member = memberRepository.findByMemberCode(memberCode).orElseThrow(() -> new MemberNotFoundException(memberCode));

        MemberPhone phone = new MemberPhone(phoneNumber, phoneType, primary, description);

        member.addPhone(phone);

        return memberRepository.save(member);
    }

    public Member removePhone(String memberCode, String phoneNumber) {
        Member member = memberRepository.findByMemberCode(memberCode).orElseThrow(() -> new MemberNotFoundException(memberCode));

        MemberPhone phone = member.getPhones().stream()
                .filter(p -> p.getPhoneNumber().equals(phoneNumber))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Phone not found: " + phoneNumber));

        member.removePhone(phone);

        return memberRepository.save(member);
    }
}