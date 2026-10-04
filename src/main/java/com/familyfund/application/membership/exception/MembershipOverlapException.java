package com.familyfund.application.membership.exception;

public class MembershipOverlapException extends RuntimeException {

    public MembershipOverlapException() {
        super("Membership overlaps with an existing membership");
    }
}