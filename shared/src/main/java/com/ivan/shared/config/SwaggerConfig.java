package com.ivan.shared.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition()
@ComponentScan("com.ivan")
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi customOpenAPI () {
        return GroupedOpenApi.builder()
                .group("api")
                .pathsToMatch("/**")
                .build();
    }
}
