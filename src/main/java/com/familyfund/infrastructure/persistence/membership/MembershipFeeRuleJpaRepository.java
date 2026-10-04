package com.familyfund.infrastructure.persistence.membership;

import com.familyfund.domain.membership.MembershipFeeRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface MembershipFeeRuleJpaRepository extends JpaRepository<MembershipFeeRule, Long> {

    @Query("""
            SELECT r
            FROM MembershipFeeRule r
            WHERE r.effectiveFrom <= :date
              AND (r.effectiveTo IS NULL OR r.effectiveTo >= :date)
            ORDER BY r.effectiveFrom DESC
            """)
    Optional<MembershipFeeRule> findEffectiveRule(@Param("date") LocalDate date);

    @Query("""
    SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END
    FROM MembershipFeeRule r
    WHERE
        (
            :effectiveTo IS NULL
            AND (
                r.effectiveTo IS NULL
                OR r.effectiveTo >= :effectiveFrom
            )
        )
        OR
        (
            :effectiveTo IS NOT NULL
            AND r.effectiveFrom <= :effectiveTo
            AND (
                r.effectiveTo IS NULL
                OR r.effectiveTo >= :effectiveFrom
            )
        )
    """)
    boolean existsOverlappingRule(
            @Param("effectiveFrom") LocalDate effectiveFrom,
            @Param("effectiveTo") LocalDate effectiveTo
    );
}