package com.familyfund.infrastructure.persistence.membership;

import com.familyfund.domain.membership.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface MembershipJpaRepository extends JpaRepository<Membership, Long> {

    @Query("""
        SELECT m
        FROM Membership m
        WHERE m.member.id = :memberId
          AND m.status = com.familyfund.domain.membership.MembershipStatus.ACTIVE
        """)
    Optional<Membership> findActiveMembershipByMemberId(@Param("memberId") Long memberId);

    @Query("""
        SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END
        FROM Membership m
        WHERE m.member.id = :memberId
          AND (:endDate IS NULL OR m.startDate <= :endDate)
          AND (m.endDate IS NULL OR m.endDate >= :startDate)
        """)
    boolean existsOverlappingMembership(
            @Param("memberId") Long memberId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}