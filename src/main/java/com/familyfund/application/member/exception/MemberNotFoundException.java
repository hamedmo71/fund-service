package com.familyfund.application.member.exception;

public class MemberNotFoundException extends RuntimeException {

    public MemberNotFoundException(String memberCode) {
        super("Member not found with code: " + memberCode);
    }
}