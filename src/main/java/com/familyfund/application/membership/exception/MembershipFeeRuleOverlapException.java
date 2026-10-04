package com.familyfund.application.membership.exception;

public class MembershipFeeRuleOverlapException
        extends RuntimeException {

    public MembershipFeeRuleOverlapException() {
        super("Membership fee rule overlaps with an existing rule");
    }
}