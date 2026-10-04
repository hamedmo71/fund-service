package com.familyfund.application.membership.port;

import com.familyfund.domain.membership.Membership;

import java.time.LocalDate;
import java.util.Optional;

public interface MembershipRepository {

    Membership save(Membership membership);

    Optional<Membership> findActiveMembershipByMemberId(Long memberId);

    boolean existsOverlappingMembership(Long memberId, LocalDate startDate, LocalDate endDate);
}