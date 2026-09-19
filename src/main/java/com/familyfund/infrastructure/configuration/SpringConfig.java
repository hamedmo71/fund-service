package com.familyfund.infrastructure.configuration;

import com.familyfund.application.member.MemberService;
import com.familyfund.application.member.port.MemberRepository;
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
}