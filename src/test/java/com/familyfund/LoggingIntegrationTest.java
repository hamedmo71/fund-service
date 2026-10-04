package com.familyfund;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringJUnitConfig(TestSpringConfig.class)
class LoggingIntegrationTest {

    private static final Logger log =
            LoggerFactory.getLogger(LoggingIntegrationTest.class);

    @Test
    void shouldInitializeLogging() {
        assertNotNull(log);

        log.info("Logging integration test executed successfully");
    }
}