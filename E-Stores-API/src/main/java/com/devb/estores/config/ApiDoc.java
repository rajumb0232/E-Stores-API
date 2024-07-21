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
                .description("")
                .version("fkv1");
    }

    @Bean
    OpenAPI openAPI(){
        return new OpenAPI().info(info());
    }

}
