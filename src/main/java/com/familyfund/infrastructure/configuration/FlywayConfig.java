package com.familyfund.infrastructure.configuration;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
public class FlywayConfig {

    @Bean(initMethod = "migrate")
    public Flyway flyway(DataSource dataSource, Environment environment) {

        boolean enabled = environment.getProperty("flyway.enabled", Boolean.class, true);

        if (!enabled) {
            return Flyway.configure().dataSource(dataSource).load();
        }

        String locations = environment.getProperty("flyway.locations", "classpath:db/migration");

        return Flyway.configure().dataSource(dataSource).locations(locations).baselineOnMigrate(environment.getProperty("flyway.baseline-on-migrate", Boolean.class, true)).load();
    }
}