package com.finalproject.carpool.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@SecurityScheme(
        name = "Authorization",
        type = SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.HEADER,
        scheme = "basic"
)
@Configuration
public class OpenApiConfig {
    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("api")
                .pathsToMatch("/api/**")
                .build();
    }
}
