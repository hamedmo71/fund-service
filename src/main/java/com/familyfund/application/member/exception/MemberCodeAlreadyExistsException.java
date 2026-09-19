package com.familyfund.application.member.exception;

public class MemberCodeAlreadyExistsException extends RuntimeException {

    public MemberCodeAlreadyExistsException(String memberCode) {
        super("Member code already exists: " + memberCode);
    }
}