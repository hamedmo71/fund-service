package com.familyfund.infrastructure.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.familyfund.infrastructure.persistence")
public class PersistenceConfig {

    @Bean
    public DataSource dataSource(Environment environment) {

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(environment.getRequiredProperty("db.url"));

        config.setUsername(environment.getRequiredProperty("db.username"));

        config.setPassword(environment.getRequiredProperty("db.password"));

        config.setMaximumPoolSize(environment.getProperty("db.pool.maximum-size", Integer.class, 10));

        config.setMinimumIdle(environment.getProperty("db.pool.minimum-idle", Integer.class, 2));

        config.setConnectionTimeout(environment.getProperty("db.pool.connection-timeout", Long.class, 30000L));

        config.setIdleTimeout(environment.getProperty("db.pool.idle-timeout", Long.class, 600000L));

        config.setMaxLifetime(environment.getProperty("db.pool.max-lifetime", Long.class, 1800000L));

        return new HikariDataSource(config);
    }

    @Bean
    @DependsOn("flyway")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, Environment environment) {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();

        factory.setDataSource(dataSource);
        factory.setJpaVendorAdapter(vendorAdapter);

        /*
         * All JPA entities are currently located
         * under the domain package.
         */
        factory.setPackagesToScan("com.familyfund.domain");

        Properties properties = new Properties();

        properties.setProperty("hibernate.hbm2ddl.auto", environment.getProperty("jpa.hibernate.ddl-auto", "validate"));

        properties.setProperty("hibernate.show_sql", environment.getProperty("jpa.show-sql", "false"));

        properties.setProperty("hibernate.format_sql", environment.getProperty("jpa.format-sql", "true"));

        properties.setProperty("hibernate.jdbc.time_zone", "Asia/Tehran");

        factory.setJpaProperties(properties);

        return factory;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {

        return new JpaTransactionManager(entityManagerFactory);
    }
}