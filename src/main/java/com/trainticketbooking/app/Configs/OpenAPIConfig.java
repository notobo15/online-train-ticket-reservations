package com.trainticketbooking.app.Configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI( @Value("${openapi.service.title}") String title,
                                  @Value("${openapi.service.version}") String version,
                                  @Value("${openapi.service.description}") String description,
                                  @Value("${openapi.service.server}") String serverUrl) {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title(title)
                        .version(version)
                        .description(description))
                        .servers(List.of( new Server().url(serverUrl)))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                        .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")));
    }

    @Bean
    public GroupedOpenApi groupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("api-docs")
                .packagesToScan("com.trainticketbooking.app.Controllers.API")
                .build();
    }
}