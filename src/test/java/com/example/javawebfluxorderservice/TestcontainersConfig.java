package com.example.javawebfluxorderservice;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.MountableFile;

@TestConfiguration
public class TestcontainersConfig {

    @Bean
    @ServiceConnection
    public PostgreSQLContainer<?> postgreSQLContainer() {
        return new PostgreSQLContainer<>("postgres:17-alpine")
//                .withDatabaseName("testdb")
//                .withUsername("test")
//                .withPassword("test")
                .withCopyFileToContainer(
                        MountableFile.forClasspathResource("schema.sql"),
                        "/docker-entrypoint-initdb.d/schema.sql"
                );
//                .withReuse(true)
//                .withLabel("com.testcontainers.desktop.service", "postgres");
    }
}
