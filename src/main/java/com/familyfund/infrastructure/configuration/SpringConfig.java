package com.familyfund.infrastructure.configuration;

import com.familyfund.application.member.MemberService;
import com.familyfund.application.member.port.MemberRepository;
import com.familyfund.application.membership.MembershipFeeRuleService;
import com.familyfund.application.membership.MembershipService;
import com.familyfund.application.membership.port.MembershipFeeRuleRepository;
import com.familyfund.application.membership.port.MembershipRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("com.familyfund")
@PropertySource("classpath:application.properties")
public class SpringConfig {

    @Bean
    public MemberService memberService(MemberRepository memberRepository) {
        return new MemberService(memberRepository);
    }

    @Bean
    public MembershipFeeRuleService membershipFeeRuleService(MembershipFeeRuleRepository repository) {

        return new MembershipFeeRuleService(repository);
    }

    @Bean
    public MembershipService membershipService(MembershipRepository membershipRepository, MemberRepository memberRepository) {

        return new MembershipService(membershipRepository, memberRepository);
    }
}