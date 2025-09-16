package com.example.javawebfluxorderservice;

//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import org.testcontainers.utility.MountableFile;
//
//@Testcontainers
//public abstract class AbstractIntegrationTest {
//
//    @Container
//    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17-alpine")
//            .withDatabaseName("testdb")
//            .withUsername("test")
//            .withPassword("test")
//            .withCopyFileToContainer(
//                    MountableFile.forClasspathResource("schema.sql"),
//                    "/docker-entrypoint-initdb.d/schema.sql"
//            );
//
//    @DynamicPropertySource
//    static void registerPgProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.r2dbc.url", () ->
//                "r2dbc:postgresql://" +
//                postgreSQLContainer.getHost() + ":" +
//                postgreSQLContainer.getFirstMappedPort() + "/" +
//                postgreSQLContainer.getDatabaseName());
//        registry.add("spring.r2dbc.username", postgreSQLContainer::getUsername);
//        registry.add("spring.r2dbc.password", postgreSQLContainer::getPassword);
//    }
//}
