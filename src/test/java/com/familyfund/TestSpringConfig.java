package com.familyfund;

import com.familyfund.infrastructure.configuration.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        SpringConfig.class,
        PersistenceConfig.class,
        FlywayConfig.class
})
public class TestSpringConfig {
}