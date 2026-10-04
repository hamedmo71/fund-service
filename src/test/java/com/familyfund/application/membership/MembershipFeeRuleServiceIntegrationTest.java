package com.familyfund.application.membership;

import com.familyfund.TestSpringConfig;
import com.familyfund.application.membership.port.MembershipFeeRuleRepository;
import com.familyfund.infrastructure.persistence.membership.adapter.MembershipFeeRuleRepositoryAdapter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(TestSpringConfig.class)
@TestPropertySource("classpath:application-test.properties")
class MembershipFeeRuleServiceIntegrationTest {

    @Autowired
    private MembershipFeeRuleService service;

    @Autowired
    private MembershipFeeRuleRepository repository;

    @Test
    void shouldCreateMembershipFeeRuleServiceBean() {
        assertNotNull(service);
    }

    @Test
    void shouldInjectMembershipFeeRuleRepositoryAdapter() {
        assertNotNull(repository);
        assertInstanceOf(
                MembershipFeeRuleRepositoryAdapter.class,
                repository
        );
    }
}