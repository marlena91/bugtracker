package com.marlena.bugtracker.config;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.migration.JavaMigration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FlywayConfiguration implements CommandLineRunner {

    private JavaMigration[] javaMigrations;
    private DataSource dataSource;
    @Autowired
    public FlywayConfiguration(DataSource dataSource) {
        this.dataSource = dataSource;
        Flyway.configure().baselineOnMigrate(true).dataSource(dataSource).load().migrate();
    }
    @Autowired
    public void setJavaMigrations(JavaMigration[] javaMigrations) {
        this.javaMigrations = javaMigrations;
    }

    @Override
    public void run(String... args) {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .javaMigrations(javaMigrations)
                .load();

        flyway.migrate();
    }
}