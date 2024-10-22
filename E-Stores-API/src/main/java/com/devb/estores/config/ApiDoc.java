package com.devb.estores.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition
public class ApiDoc {

    private Info info(){
        return new Info().title("E-Stores API")
                .description("-\tDeveloped a backend RESTful API using Spring Boot, Spring Data JPA for PostgreSQL, and Spring Data Mongo for MongoDB to facilitate online orders for local stores.\n" +
                        "-\tIntegrated Spring Security with JWT for secure authentication, implementing best practices to protect against XSS vulnerabilities.\n" +
                        "-\tThe frontend UI was built using ReactJS, React Router Dom, and JavaScript, with responsive design and smooth user navigation.\n\n" +
                        "Tech-Stack - Spring Boot, Spring Data JPA, Swagger (OpenAPI), PostgreSQL-Database, Spring Security and JWT. ")
                .version("fkv1");
    }

    @Bean
    OpenAPI openAPI(){
        return new OpenAPI().info(info());
    }

}
