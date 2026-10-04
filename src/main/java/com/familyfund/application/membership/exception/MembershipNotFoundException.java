package com.familyfund.application.membership.exception;

public class MembershipNotFoundException extends RuntimeException {

    public MembershipNotFoundException(String memberCode) {
        super("Active membership not found for member: " + memberCode);
    }
}